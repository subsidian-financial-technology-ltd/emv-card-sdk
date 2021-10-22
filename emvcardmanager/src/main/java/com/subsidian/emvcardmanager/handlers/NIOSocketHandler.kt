package com.subsidian.emvcardmanager.handlers

import kotlin.Throws
import com.subsidian.emvcardmanager.exceptions.ISOClientException
import com.subsidian.emvcardmanager.interfaces.ISOClientEventListener
import com.subsidian.emvcardmanager.interfaces.SocketHandler
import com.subsidian.emvcardmanager.utils.StringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.net.InetSocketAddress
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import javax.net.ssl.SSLEngine
import javax.net.ssl.SSLEngineResult
import javax.net.ssl.SSLException

class NIOSocketHandler : SocketHandler {
    private var socketChannel: SocketChannel? = null
    private var myAppData: ByteBuffer? = null
    private var myNetData: ByteBuffer? = null
    private var peerAppData: ByteBuffer? = null
    private var peerNetData: ByteBuffer? = null
    private var engine: SSLEngine? = null
    private var sslHandler: SSLHandler? = null

    @Throws(ISOClientException::class)
    override fun init(
        host: String,
        port: Int,
        isoClientEventListener: ISOClientEventListener?,
        sslHandler: SSLHandler?
    ) {
        try {
            this@NIOSocketHandler.sslHandler = sslHandler
            engine = sslHandler!!.context.createSSLEngine(host, port)
            engine!!.useClientMode = true
            engine!!.needClientAuth = false
            socketChannel = SocketChannel.open()
            socketChannel!!.configureBlocking(false)
            socketChannel!!.connect(InetSocketAddress(host, port))
            while (!socketChannel!!.finishConnect()) {
            }

            // Create byte buffers to use for holding application and encoded data
            val session = engine!!.session
            myAppData = ByteBuffer.allocate(session.applicationBufferSize)
            myNetData = ByteBuffer.allocate(session.packetBufferSize)
            peerAppData = ByteBuffer.allocate(session.applicationBufferSize)
            peerNetData = ByteBuffer.allocate(session.packetBufferSize)
            val connected = sslHandler.doHandshake(
                socketChannel!!,
                engine!!,
                myNetData!!,
                peerNetData!!,
                peerAppData!!,
                myAppData
            )
            if (!connected) throw ISOClientException("Handshake not performed well")
            postInit()
        } catch (e: Exception) {
            throw ISOClientException(e)
        }
    }

    @Throws(IOException::class)
    override fun init(host: String, port: Int, isoClientEventListener: ISOClientEventListener?) {
        socketChannel = SocketChannel.open()
        socketChannel!!.configureBlocking(false)
        socketChannel!!.connect(InetSocketAddress(host, port))
        while (!socketChannel!!.finishConnect()) {
        }
        myAppData = ByteBuffer.allocate(1024)
        postInit()
    }

    @Throws(SocketException::class)
    private fun postInit() {
        socketChannel!!.socket().soTimeout = 60000
    }

    @Throws(IOException::class)
    override fun sendMessageSync(buffer: ByteBuffer, length: Int): ByteArray {
        return if (sslHandler != null) {
            val data = sendMessageSyncOverSsl(buffer)
            Arrays.copyOfRange(data, if (length > 0) length else 0, data.size)
        } else {
            myAppData!!.clear()
            myAppData!!.put(buffer.array())
            myAppData!!.flip()
            while (myAppData!!.hasRemaining()) {
                socketChannel!!.write(myAppData)
            }
            myAppData!!.clear()
            myAppData!!.compact()
            myAppData!!.flip()
            var r: Int
            do {
                r = socketChannel!!.read(myAppData)
            } while (myAppData!!.remaining() >= 0 && r == 0)
            if (myAppData!!.position() > length) Arrays.copyOfRange(
                myAppData!!.array(),
                if (length > 0) length else 0,
                myAppData!!.position()
            ) else ByteArray(0)
        }
    }

    @Throws(IOException::class)
    private fun sendMessageSyncOverSsl(buffer: ByteBuffer): ByteArray {
        write(buffer)
        return read()
    }

    @Throws(IOException::class)
    private fun write(buffer: ByteBuffer) {
        myAppData!!.clear()
        myAppData!!.put(buffer.array())
        myAppData!!.flip()
        while (myAppData!!.hasRemaining()) {
            // The loop has a meaning for (outgoing) messages larger than 16KB.
            // Every wrap call will remove 16KB from the original label and send it to the remote peer.
            myNetData!!.clear()
            val result = engine!!.wrap(myAppData, myNetData)
            when (result.status) {
                SSLEngineResult.Status.OK -> {
                    myNetData!!.flip()
                    while (myNetData!!.hasRemaining()) {
                        socketChannel!!.write(myNetData)
                    }
                    println("Message sent to the server: " + StringUtil.fromByteArray(myAppData!!.array()))
                }
                SSLEngineResult.Status.BUFFER_OVERFLOW -> myNetData =
                    sslHandler!!.enlargePacketBuffer(
                        engine!!, myNetData!!
                    )
                SSLEngineResult.Status.BUFFER_UNDERFLOW -> throw SSLException("Buffer underflow occured after a wrap. I don't think we should ever get here.")
                SSLEngineResult.Status.CLOSED -> {
                    close()
                    return
                }
                else -> throw IllegalStateException("Invalid SSL status: " + result.status)
            }
        }
    }

    @Throws(IOException::class)
    private fun read(): ByteArray {
        var response = ByteArray(0)
        peerNetData!!.clear()
        val waitToReadMillis = 50
        var exitReadLoop = false
        while (!exitReadLoop) {
            val bytesRead = socketChannel!!.read(peerNetData)
            if (bytesRead > 0) {
                peerNetData!!.flip()
                while (peerNetData!!.hasRemaining()) {
                    peerAppData!!.clear()
                    val result = engine!!.unwrap(peerNetData, peerAppData)
                    when (result.status) {
                        SSLEngineResult.Status.OK -> {
                            peerAppData!!.flip()
                            val resp = ByteArray(peerAppData!!.limit())
                            var i = 0
                            while (i < resp.size) {
                                resp[i] = peerAppData!![i]
                                i++
                            }
                            response = resp
                            exitReadLoop = true
                        }
                        SSLEngineResult.Status.BUFFER_OVERFLOW -> peerAppData =
                            sslHandler!!.enlargeApplicationBuffer(
                                engine!!, peerAppData!!
                            )
                        SSLEngineResult.Status.BUFFER_UNDERFLOW -> peerNetData =
                            sslHandler!!.handleBufferUnderflow(
                                engine!!, peerNetData!!
                            )
                        SSLEngineResult.Status.CLOSED -> {
                            close()
                            return response
                        }
                        else -> throw IllegalStateException("Invalid SSL status: " + result.status)
                    }
                }
            } else if (bytesRead < 0) {
                engine!!.closeInbound()
                return response
            }
            try {
                Thread.sleep(waitToReadMillis.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return response
    }

    override fun close() {
        try {
            if (socketChannel != null) socketChannel!!.close()
            if (myAppData != null) {
                myAppData!!.compact()
                myAppData!!.clear()
                myAppData = null
            }
            if (myNetData != null) {
                myNetData!!.compact()
                myNetData!!.clear()
                myNetData = null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(SocketException::class)
    override fun setReadTimeout(readTimeout: Int) {
        socketChannel!!.socket().soTimeout = 60000
    }

    override fun isConnected(): Boolean {
        return socketChannel != null && socketChannel!!.isConnected
    }

    override fun isClosed(): Boolean {
        return socketChannel == null || socketChannel!!.socket().isClosed
    }
}