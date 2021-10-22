package com.subsidian.emvcardmanager.interfaces

import kotlin.Throws
import com.subsidian.emvcardmanager.exceptions.ISOClientException
import com.subsidian.emvcardmanager.handlers.SSLHandler
import java.io.IOException
import java.net.SocketException
import java.nio.ByteBuffer

/**
 * <h1>Socket Handler Interface</h1>
 * Its responsible about initializing socket and connection to ISO switch
 * @author subsidian
 */
interface SocketHandler {
    /**
     * Initialize SSL connection to switch
     * @param host IP address of switch
     * @param port Switch port number
     * @param isoClientEventListener Event listener for dispatch state of operation
     * @param sslHandler Implementation of [SSLHandler] for handling ssl handshakes
     * @throws ISOClientException
     */
    @Throws(ISOClientException::class)
    fun init(
        host: String,
        port: Int,
        isoClientEventListener: ISOClientEventListener?,
        sslHandler: SSLHandler?
    )

    /**
     * Initialize NONE SSL connection to switch
     * @param host IP address of switch
     * @param port Switch port number
     * @param isoClientEventListener Event listener for dispatch state of operation
     * @throws IOException
     */
    @Throws(IOException::class)
    fun init(host: String, port: Int, isoClientEventListener: ISOClientEventListener?)

    /**
     * Send message in sync way and return result
     * @param buffer buffer for sending
     * @param length  length of message length
     * @return response buffer from message
     * @throws IOException
     * @throws ISOClientException
     */
    @Throws(IOException::class, ISOClientException::class)
    fun sendMessageSync(buffer: ByteBuffer, length: Int): ByteArray?

    /**
     * Close current socket
     */
    fun close()

    /**
     * Set waiting time for take a response from switch
     * @param readTimeout time out in milliseconds
     * @throws SocketException
     */
    @Throws(SocketException::class)
    fun setReadTimeout(readTimeout: Int)

    /**
     * Check socket already connected to the host.
     * @return true if is connected
     */
    fun isConnected(): Boolean

    /**
     * Check if socket is closed.
     * @return true if socket already closed
     */
    fun isClosed(): Boolean
}