package com.subsidian.emvcardmanager.entities

import com.solab.iso8583.IsoMessage

class ISOMessage() {

    private var isoMessage: IsoMessage = IsoMessage()

    constructor(isoMessage: IsoMessage) : this() {
        this.isoMessage = isoMessage
    }

    fun getMessage(): IsoMessage {
        return this.isoMessage
    }

    fun getMessageType(formatType: MessageTypeFormat = MessageTypeFormat.HEXA): String {
        return this.isoMessage.type.toString(formatType.value)
    }

    companion object {
        enum class MessageTypeFormat(val value: Int){
            HEXA(16),
            DECIMAL(10),
            OCTAL(8),
            BINARY(2)
        }
    }
}