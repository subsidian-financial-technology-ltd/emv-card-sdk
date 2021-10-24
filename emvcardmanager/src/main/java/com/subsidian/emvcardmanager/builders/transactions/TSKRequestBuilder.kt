package com.subsidian.emvcardmanager.builders.transactions

import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.entities.ISOMessage
import com.subsidian.emvcardmanager.enums.ISOMessageType

object TSKRequestBuilder {

    fun build (isoData: ISOData, messageFactory: MessageFactory<IsoMessage>): ISOMessage {
        val type: Int = ISOMessageType._0800.value.toInt(16)
        val message: IsoMessage = messageFactory.newMessage(type)
        val templ: IsoMessage = messageFactory.getMessageTemplate(type)
        /** Set ProcCode **/
        message.setValue(
            3,
            isoData.processingCode,
            templ.getField<Any>(3).type,
            templ.getField<Any>(3).length
        )
        /** Set Transaction Time and Date **/
        message.setValue(
            7,
            isoData.transmissionDateTime,
            templ.getField<Any>(7).type,
            templ.getField<Any>(7).length
        )
        /** Set STAN **/
        message.setValue(
            11,
            isoData.systemTraceAuditNumber,
            templ.getField<Any>(11).type,
            templ.getField<Any>(11).length
        )
        /** Set Local Time **/
        message.setValue(
            12,
            isoData.localTime,
            templ.getField<Any>(12).type,
            templ.getField<Any>(12).length
        )
        /** Set Local Date **/
        message.setValue(
            13,
            isoData.localDate,
            templ.getField<Any>(13).type,
            templ.getField<Any>(13).length
        )
        /** Set Terminal ID **/
        message.setValue(
            41,
            isoData.cardAcceptorTerminalId,
            templ.getField<Any>(41).type,
            templ.getField<Any>(41).length
        )
        return ISOMessage(message)
    }

}