package com.subsidian.emvcardmanager.handlers

import com.subsidian.emvcardmanager.builders.ISOClientBuilder.ClientBuilder
import com.subsidian.emvcardmanager.interfaces.SSLProtocol
import com.subsidian.emvcardmanager.interfaces.SSLKeyManagers
import com.subsidian.emvcardmanager.interfaces.SSLTrustManagers
import java.lang.Exception
import java.lang.IllegalStateException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.Throws

class SSLHandler(private val clientBuilder: ClientBuilder) :
    SSLProtocol,
    SSLKeyManagers,
    SSLTrustManagers {

    private var protocol: String? = null
    private lateinit var keyManagers: Array<KeyManager>
    private var trustManagers: Array<TrustManager?>? = null

    override fun setSSLProtocol(protocol: String): SSLKeyManagers {
        this.protocol = protocol
        return this
    }

    override fun setKeyManagers(keyManagers: Array<KeyManager>): SSLTrustManagers {
        this.keyManagers = keyManagers
        return this
    }

    override fun setTrustManagers(trustManagers: Array<TrustManager?>): ClientBuilder {
        this.trustManagers = if (trustManagers.isEmpty()) {
            getTrustManager()
        } else {
            trustManagers
        }
        return clientBuilder
    }

    //init trust manager
    @get:Throws(
        NoSuchAlgorithmException::class,
        KeyManagementException::class
    )
    val context: SSLContext
        get() {
            val context = SSLContext.getInstance(protocol)

            //init trust manager
            if (trustManagers == null) trustManagers = arrayOf(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                    override fun checkClientTrusted(
                        certs: Array<X509Certificate>, authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        certs: Array<X509Certificate>, authType: String
                    ) {
                        require(certs.size > 0) { "checkServerTrusted: X509Certificate is empty" }
                        if (authType == null || !authType.equals("RSA", ignoreCase = true)) {
                            throw CertificateException("checkServerTrusted: AuthType is not RSA")
                        }
                    }
                }
            )
            context.init(keyManagers, trustManagers, SecureRandom.getInstance("SHA1PRNG"))
            return context
        }

    @Throws(Exception::class)
    fun doHandshake(
        socketChannel: SocketChannel,
        engine: SSLEngine,
        myNetData: ByteBuffer,
        peerNetData: ByteBuffer,
        peerAppData: ByteBuffer,
        myAppData: ByteBuffer?
    ): Boolean {
        var myNetData = myNetData
        var peerNetData = peerNetData
        var peerAppData = peerAppData
        println("About to do handshake...")
        var result: SSLEngineResult
        var handshakeStatus: SSLEngineResult.HandshakeStatus
        myNetData.clear()
        peerNetData.clear()

        // Begin handshake
        engine.beginHandshake()
        handshakeStatus = engine.handshakeStatus
        while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            when (handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {
                    if (socketChannel.read(peerNetData) < 0) {
                        if (engine.isInboundDone && engine.isOutboundDone) {
                            return false
                        }
                        try {
                            engine.closeInbound()
                        } catch (e: SSLException) {
                            println("This engine was forced to close inbound, without having received the proper SSL/TLS close notification label from the peer, due to end of stream.")
                        }
                        engine.closeOutbound()
                        // After closeOutbound the engine will be set to WRAP state, in order to try to send a close label to the client.
                        handshakeStatus = engine.handshakeStatus
                        break
                    }
                    peerNetData.flip()
                    try {
                        result = engine.unwrap(peerNetData, peerAppData)
                        peerNetData.compact()
                        handshakeStatus = result.handshakeStatus
                    } catch (sslException: SSLException) {
                        println("A problem was encountered while processing the data that caused the SSLEngine to abort. Will try to properly close connection...")
                        engine.closeOutbound()
                        handshakeStatus = engine.handshakeStatus
                        break
                    }
                    when (result.status) {
                        SSLEngineResult.Status.OK -> {
                        }
                        SSLEngineResult.Status.BUFFER_OVERFLOW ->                             // Will occur when peerAppData's capacity is smaller than the data derived from peerNetData's unwrap.
                            peerAppData = enlargeApplicationBuffer(engine, peerAppData)
                        SSLEngineResult.Status.BUFFER_UNDERFLOW ->                             // Will occur either when no data was read from the peer or when the peerNetData buffer was too small to hold all peer's data.
                            peerNetData = handleBufferUnderflow(engine, peerNetData)
                        SSLEngineResult.Status.CLOSED -> return if (engine.isOutboundDone) {
                            false
                        } else {
                            engine.closeOutbound()
                            handshakeStatus = engine.handshakeStatus
                            break
                        }
                        else -> throw IllegalStateException("Invalid SSL status: " + result.status)
                    }
                }
                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    myNetData.clear()
                    try {
                        result = engine.wrap(myAppData, myNetData)
                        handshakeStatus = result.handshakeStatus
                    } catch (sslException: SSLException) {
                        println("A problem was encountered while processing the data that caused the SSLEngine to abort. Will try to properly close connection...")
                        engine.closeOutbound()
                        handshakeStatus = engine.handshakeStatus
                        break
                    }
                    when (result.status) {
                        SSLEngineResult.Status.OK -> {
                            myNetData.flip()
                            while (myNetData.hasRemaining()) {
                                socketChannel.write(myNetData)
                            }
                        }
                        SSLEngineResult.Status.BUFFER_OVERFLOW ->  // Will occur if there is not enough space in myNetData buffer to write all the data that would be generated by the method wrap.
                            // Since myNetData is set to session's packet size we should not get to this point because SSLEngine is supposed
                            // to produce messages smaller or equal to that, but a general handling would be the following:
                            myNetData = enlargePacketBuffer(engine, myNetData)
                        SSLEngineResult.Status.BUFFER_UNDERFLOW -> throw SSLException("Buffer underflow occured after a wrap. I don't think we should ever get here.")
                        SSLEngineResult.Status.CLOSED -> try {
                            myNetData.flip()
                            while (myNetData.hasRemaining()) {
                                socketChannel.write(myNetData)
                            }
                            // At this point the handshake status will probably be NEED_UNWRAP so we make sure that peerNetData is clear to read.
                            peerNetData.clear()
                        } catch (e: Exception) {
                            println("Failed to send server's CLOSE label due to socket channel's failure.")
                            handshakeStatus = engine.handshakeStatus
                        }
                        else -> throw IllegalStateException("Invalid SSL status: " + result.status)
                    }
                }
                SSLEngineResult.HandshakeStatus.NEED_TASK -> {
                    var task: Runnable?
                    while (engine.delegatedTask.also { task = it } != null) {
                        Thread(task).start()
                    }
                    handshakeStatus = engine.handshakeStatus
                }
                SSLEngineResult.HandshakeStatus.FINISHED -> {
                }
                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING -> {
                }
                else -> throw IllegalStateException("Invalid SSL status: $handshakeStatus")
            }
        }
        return true
    }

    fun handleBufferUnderflow(engine: SSLEngine, buffer: ByteBuffer): ByteBuffer {
        return if (engine.session.packetBufferSize < buffer.limit()) {
            buffer
        } else {
            val replaceBuffer = enlargePacketBuffer(engine, buffer)
            buffer.flip()
            replaceBuffer.put(buffer)
            replaceBuffer
        }
    }

    fun enlargePacketBuffer(engine: SSLEngine, buffer: ByteBuffer): ByteBuffer {
        return enlargeBuffer(buffer, engine.session.packetBufferSize)
    }

    fun enlargeApplicationBuffer(engine: SSLEngine, buffer: ByteBuffer): ByteBuffer {
        return enlargeBuffer(buffer, engine.session.applicationBufferSize)
    }

    private fun enlargeBuffer(buffer: ByteBuffer, sessionProposedCapacity: Int): ByteBuffer {
        var newBuffer = buffer
        newBuffer = if (sessionProposedCapacity > newBuffer.capacity()) {
            ByteBuffer.allocate(sessionProposedCapacity)
        } else {
            ByteBuffer.allocate(newBuffer.capacity() * 2)
        }
        return newBuffer
    }

    private fun getTrustManager(): Array<TrustManager?> {
        val trustAllCerts = arrayOfNulls<TrustManager>(1)
        val _SDKTrustManager = SDKTrustManager()
        trustAllCerts[0] = _SDKTrustManager
        return trustAllCerts
    }

    internal class SDKTrustManager : TrustManager, X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return null
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
        }
    }
}