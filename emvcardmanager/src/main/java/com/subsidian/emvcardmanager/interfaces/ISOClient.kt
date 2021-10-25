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
     * @param packBuilder
     * @return
     * @throws ISOClientException
     * @throws IOException
     */
    @Throws(ISOClientException::class, IOException::class)
    fun sendMessageSync(packBuilder: ISOMessageBuilder.PackBuilder): ByteArray

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