package com.subsidian.emvcardmanager.builders

import com.solab.iso8583.IsoMessage
import com.subsidian.emvcardmanager.exceptions.ISOClientException
import com.subsidian.emvcardmanager.handlers.IOSocketHandler
import com.subsidian.emvcardmanager.handlers.NIOSocketHandler
import com.subsidian.emvcardmanager.handlers.SSLHandler
import com.subsidian.emvcardmanager.interfaces.ISOClient
import com.subsidian.emvcardmanager.interfaces.ISOClientEventListener
import com.subsidian.emvcardmanager.interfaces.SSLProtocol
import com.subsidian.emvcardmanager.interfaces.SocketHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

object ISOClientBuilder {

    private var clientBuilder: ClientBuilder? = null

    fun createSocket(host: String, port: Int): ClientBuilder {
        clientBuilder = ClientBuilder(host, port)
        return clientBuilder!!
    }

    /**
     * ClientBuilder
     */
    class ClientBuilder(host: String, port: Int) {

        private val client: DefaultISOClient = DefaultISOClient()

        /**
         * Sending with NIO (false) or Blocking IO (true)
         * @param blocking:true
         * @return [ClientBuilder]
         */
        fun configureBlocking(blocking: Boolean): ClientBuilder {
            client.setBlocking(blocking)
            return this
        }

        /**
         * Enable sending over SSL/TLS
         * @return [ClientBuilder]
         */
        fun enableSSL(): SSLProtocol {
            return client.enableSSL(SSLHandler(this))
        }

        /**
         * Build ISOClient for sending label
         * @return [ClientBuilder]
         */
        fun build(): ISOClient {
            return client
        }

        /**
         * set Timeout for read from socket
         * @param millisecond timeout in millisecond
         * @return [ClientBuilder]
         */
        fun setReadTimeout(millisecond: Int): ClientBuilder {
            client.setReadTimeout(millisecond)
            return this
        }

        /**
         * Set Message length in Byte
         * @param bytes default: 2 byte
         * @return [ClientBuilder]
         */
        fun length(bytes: Int): ClientBuilder {
            client.setLength(bytes)
            return this
        }

        /**
         * Set event listener for dispatch events
         * @param eventListener Implementation of [ISOClientEventListener]
         * @return [ClientBuilder]
         */
        fun setEventListener(eventListener: ISOClientEventListener?): ClientBuilder {
            if (eventListener != null) client.setEventListener(eventListener)
            return this
        }

        /**
         * Create ISO Client after initializing
         * @param host socket Host
         * @param port socket ip
         */
        init {
            client.setSocketAddress(host, port)
        }
    }

    class DefaultISOClient internal constructor() : ISOClient {

        private var sslHandler: SSLHandler? = null
        private var socketHandler: SocketHandler? = null
        private var buffer: ByteBuffer? = null
        private var blocking = true

        @Volatile
        private var connected = false
        private var host: String = ""
        private var port = 0
        private var readTimeout = 60000
        private var length = 2
        private val lock = Any()
        private var isoClientEventListener: ISOClientEventListener?

        @Throws(ISOClientException::class, IOException::class)
        override fun connect() {
            GlobalScope.launch(Dispatchers.IO) {
                isoClientEventListener!!.connecting()
                if (sslHandler != null) socketHandler!!.init(
                    host,
                    port,
                    isoClientEventListener,
                    sslHandler
                ) else socketHandler!!.init(host, port, isoClientEventListener)
                socketHandler!!.setReadTimeout(readTimeout)
                connected = true
                isoClientEventListener!!.connected()
            }
        }

        override fun disconnect() {
            if (socketHandler != null) socketHandler!!.close()
            if (buffer != null) {
                buffer!!.flip()
                buffer!!.put(ByteBuffer.allocate(buffer!!.limit()))
                buffer = null
            }
            connected = false
            isoClientEventListener!!.disconnected()
        }

        private fun initBuffer(isoMessage: IsoMessage): ByteBuffer {
            val len = isoMessage.writeData().size
            buffer = ByteBuffer.allocate(len + length)
            if (length > 0) {
                val mlen = ByteBuffer.allocate(4).putInt(len).array()
                buffer!!.put(Arrays.copyOfRange(mlen, 2, 4))
            }
            buffer!!.put(isoMessage.writeData())
            return buffer!!
        }

        @Throws(ISOClientException::class, IOException::class)
        override fun sendMessageSync(isoMessage: ISOMessageBuilder.ISOMessage): ByteArray {
            var result: ByteArray = ByteArray(0)
            synchronized(lock) {
                if (socketHandler == null) throw ISOClientException("Client handler init failed, unable to connect to the server!")
                if (!isConnected()) throw ISOClientException("Client unable to connect to the server!")
                val buffer: ByteBuffer = if (isoMessage.message != null){
                    initBuffer(isoMessage.message!!)
                } else {
                    ByteBuffer.allocate(0)
                }
                result = socketHandler!!.sendMessageSync(buffer, length)!!
            }
            return result
        }


        override fun isConnected(): Boolean {
            return socketHandler != null && socketHandler!!.isConnected()
        }

        override fun isClosed(): Boolean {
            return socketHandler != null && socketHandler!!.isClosed()
        }

        override fun setEventListener(isoClientEventListener: ISOClientEventListener?) {
            this.isoClientEventListener = isoClientEventListener
        }

        fun setSocketAddress(host: String, port: Int) {
            this.host = host
            this.port = port
        }

        fun enableSSL(sslHandler: SSLHandler): SSLHandler {
            this.sslHandler = sslHandler
            return sslHandler
        }

        fun setBlocking(blocking: Boolean) {
            this.blocking = blocking
        }

        fun setReadTimeout(readTimeout: Int) {
            this.readTimeout = readTimeout
        }

        fun setLength(bytes: Int) {
            this.length = bytes
        }

        init {
            socketHandler = if (blocking) {
                IOSocketHandler()
            } else {
                NIOSocketHandler()
            }
            isoClientEventListener = EmptyISOClientEventListener()
        }
    }

    private class EmptyISOClientEventListener : ISOClientEventListener {
        override fun connecting() {}
        override fun connected() {}
        override fun connectionFailed() {}
        override fun connectionClosed() {}
        override fun disconnected() {}
        override fun beforeSendingMessage() {}
        override fun afterSendingMessage() {}
        override fun onReceiveData() {}
        override fun beforeReceiveResponse() {}
        override fun afterReceiveResponse() {}
    }
}