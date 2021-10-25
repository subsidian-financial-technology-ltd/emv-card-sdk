package com.subsidian.emvcardmanager.handlers

import android.util.Log
import com.subsidian.emvcardmanager.exceptions.ISOClientException
import com.subsidian.emvcardmanager.interfaces.ISOClientEventListener
import com.subsidian.emvcardmanager.interfaces.SocketHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.nio.BufferOverflowException
import java.nio.ByteBuffer
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import kotlin.experimental.and

class IOSocketHandler : SocketHandler {

    private var socket: Socket? = null
    private var socketWriter: BufferedOutputStream? = null
    private var socketReader: BufferedInputStream? = null
    private var isoClientEventListener: ISOClientEventListener? = null

    @Throws(ISOClientException::class)
    override fun init(
        host: String,
        port: Int,
        isoClientEventListener: ISOClientEventListener?,
        sslHandler: SSLHandler?
    ) {
        this.isoClientEventListener = isoClientEventListener
        var context: SSLContext? = null
        try {
            context = sslHandler?.context
            val sslsocketfactory = context?.socketFactory
            val socket = sslsocketfactory?.createSocket(
                host, port
            ) as SSLSocket
            socket.needClientAuth = false
            socket.startHandshake()
            this@IOSocketHandler.socket = socket
            postInit()
        } catch (e: Exception) {
            throw ISOClientException(e)
        }
    }

    @Throws(IOException::class)
    override fun init(host: String, port: Int, isoClientEventListener: ISOClientEventListener?) {
        this.isoClientEventListener = isoClientEventListener
        this@IOSocketHandler.socket = Socket(host, port)
        postInit()
    }

    @Throws(IOException::class)
    private fun postInit() {
        this@IOSocketHandler.socketWriter = BufferedOutputStream(socket!!.getOutputStream())
    }

    @Throws(IOException::class, ISOClientException::class)
    override fun sendMessageSync(buffer: ByteBuffer, length: Int): ByteArray {
        val bufferCapacity = 1024
        isoClientEventListener!!.beforeSendingMessage()
        for (v in buffer.array()) {
            socketWriter!!.write(v.toInt())
        }
        socketWriter!!.flush()
        socketReader = BufferedInputStream(socket!!.getInputStream())
        isoClientEventListener!!.afterSendingMessage()
        isoClientEventListener!!.beforeReceiveResponse()
        var readBuffer = ByteBuffer.allocate(bufferCapacity)
        return try {
            if (length > 0) {
                val bLen = ByteArray(length)
                socketReader!!.read(bLen, 0, length)
                val mLen: Int = (bLen[0] and (0xff).toByte()) + (bLen[1] and (0xff).toByte())
            }
            var r: Int
            var fo = 512
            /** To handle buffer overflow **/
            if (socketReader!!.available() > bufferCapacity) {
                readBuffer.clear()
                readBuffer.compact()
                readBuffer = ByteBuffer.allocate(socketReader!!.available())
            } else if(socketReader!!.available() == 0) {
                readBuffer.clear()
                readBuffer.compact()
                readBuffer = ByteBuffer.allocate(bufferCapacity * 10)
            }
            do {
                r = socketReader!!.read()
                if (!(r == -1 && socketReader!!.available() == 0) && readBuffer.remaining() > 0) {
                    readBuffer.put(r.toByte())
                } else {
                    fo--
                }
            } while ((r > -1 && socketReader!!.available() > 0 ||
                        r == -1 && readBuffer.position() <= 1) && fo > 0)
            val resp = Arrays.copyOfRange(readBuffer.array(), 0, readBuffer.position())
            isoClientEventListener!!.afterReceiveResponse()
            resp
        }
        catch (e: SocketTimeoutException) {
            if (isoClientEventListener != null) {
                isoClientEventListener!!.connectionTimeout()
            }
            throw ISOClientException("Read Timeout")
        }
        finally {
            readBuffer.clear()
            readBuffer.compact()
        }
    }

    @Synchronized
    override fun close() {
        try {
            if (socketWriter != null) socketWriter!!.close()
            if (socketReader != null) socketReader!!.close()
            if (socket != null) socket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(SocketException::class)
    override fun setReadTimeout(readTimeout: Int) {
        socket?.soTimeout = readTimeout
    }

    override fun isConnected(): Boolean {
        return if (socket != null) socket!!.isConnected else false
    }

    override fun isClosed(): Boolean {
        return socket == null || socket!!.isClosed
    }
}