package com.subsidian.emvcardmanager.interfaces

interface ISOClientEventListener {
    fun connecting()
    fun connected()
    fun connectionFailed()
    fun connectionClosed()
    fun connectionTimeout()
    fun disconnected()
    fun beforeSendingMessage()
    fun afterSendingMessage()
    fun onReceiveData()
    fun beforeReceiveResponse()
    fun afterReceiveResponse()
}