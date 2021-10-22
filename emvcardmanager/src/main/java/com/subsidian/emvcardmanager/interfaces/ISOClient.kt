package com.subsidian.emvcardmanager.interfaces

import com.subsidian.emvcardmanager.builders.ISOMessageBuilder
import com.subsidian.emvcardmanager.exceptions.ISOClientException
import java.io.IOException

interface ISOClient {
    /**
     *
     * @throws ISOClientException
     * @throws IOException
     */
    @Throws(ISOClientException::class, IOException::class)
    fun connect()

    /**
     *
     */
    fun disconnect()

    /**
     *
     * @param isoMessage
     * @return
     * @throws ISOClientException
     * @throws IOException
     */
    @Throws(ISOClientException::class, IOException::class)
    fun sendMessageSync(isoMessage: ISOMessageBuilder.ISOMessage): ByteArray

    /**
     *
     *
     * @return
     */
    @Throws(ISOClientException::class, IOException::class)
    fun isConnected(): Boolean

    /**
     *
     * @return
     */
    @Throws(ISOClientException::class, IOException::class)
    fun isClosed(): Boolean

    /**
     *
     * @param isoClientEventListener
     */
    fun setEventListener(isoClientEventListener: ISOClientEventListener?)
}