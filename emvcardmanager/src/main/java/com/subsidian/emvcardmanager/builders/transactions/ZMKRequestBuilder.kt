package com.subsidian.emvcardmanager.builders.transactions

import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.enums.ISOMessageType
import com.subsidian.emvcardmanager.utils.StringUtil

object ZMKRequestBuilder {

    fun build (isoData: ISOData, messageFactory: MessageFactory<IsoMessage>): IsoMessage {
        val type: Int = ISOMessageType._0800.value.toInt(16)
        val message: IsoMessage = messageFactory.newMessage(type)
        val templ: IsoMessage = messageFactory.getMessageTemplate(type)
        /** Set ProcCode **/
        if (isoData.processingCode != null && !StringUtil.isEmpty(isoData.processingCode)) {
            message.setValue(
                3,
                isoData.processingCode,
                templ.getField<Any>(3).type,
                templ.getField<Any>(3).length
            )
        }
        /** Set Transmission Date Time **/
        if (isoData.transmissionDateTime != null && !StringUtil.isEmpty(isoData.transmissionDateTime)) {
            message.setValue(
                7,
                isoData.transmissionDateTime,
                templ.getField<Any>(7).type,
                templ.getField<Any>(7).length
            )
        }
        /** Set STAN **/
        if (isoData.systemTraceAuditNumber != null && !StringUtil.isEmpty(isoData.systemTraceAuditNumber)) {
            message.setValue(
                11,
                isoData.systemTraceAuditNumber,
                templ.getField<Any>(11).type,
                templ.getField<Any>(11).length
            )
        }
        /** Set Local Time **/
        if (isoData.localTime != null && !StringUtil.isEmpty(isoData.localTime)) {
            message.setValue(
                12,
                isoData.localTime,
                templ.getField<Any>(12).type,
                templ.getField<Any>(12).length
            )
        }
        /** Set Local Date **/
        if (isoData.localDate != null && !StringUtil.isEmpty(isoData.localDate)) {
            message.setValue(
                13,
                isoData.localDate,
                templ.getField<Any>(13).type,
                templ.getField<Any>(13).length
            )
        }
        /** Set Card Acceptor Terminal Code or Terminal ID **/
        if (isoData.cardAcceptorTerminalId != null && !StringUtil.isEmpty(isoData.cardAcceptorTerminalId)) {
            message.setValue(
                41,
                isoData.cardAcceptorTerminalId,
                templ.getField<Any>(41).type,
                templ.getField<Any>(41).length
            )
        }
        return message
    }

}