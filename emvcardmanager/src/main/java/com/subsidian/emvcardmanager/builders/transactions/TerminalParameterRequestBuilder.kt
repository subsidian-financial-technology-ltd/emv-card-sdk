package com.subsidian.emvcardmanager.builders.transactions

import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.entities.ISOMessage
import com.subsidian.emvcardmanager.enums.ISOMessageType
import com.subsidian.emvcardmanager.utils.KeyUtil
import com.subsidian.emvcardmanager.utils.StringUtil
import java.lang.StringBuilder

object TerminalParameterRequestBuilder {

    fun build (isoData: ISOData, messageFactory: MessageFactory<IsoMessage>, terminalSessionKey: String = ""): ISOMessage {
        val type: Int = ISOMessageType._0800.value.toInt(16)
        val message: IsoMessage = messageFactory.newMessage(type)
        val templ: IsoMessage = messageFactory.getMessageTemplate(type)
        val managementDataOne = StringBuilder().append("01")
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
            val cardAcceptorTerminalIdLength: String = StringUtil.leftPadding('0', 3, isoData.cardAcceptorTerminalId!!.length.toString())
            managementDataOne
                .append(cardAcceptorTerminalIdLength)
                .append(isoData.cardAcceptorTerminalId)
            message.setValue(
                41,
                isoData.cardAcceptorTerminalId,
                templ.getField<Any>(41).type,
                templ.getField<Any>(41).length
            )
        }
        /** Set Management Data One **/
        if (isoData.managementDataOnePrivate != null && !StringUtil.isEmpty(isoData.managementDataOnePrivate)) {
            message.setValue(
                62,
                isoData.managementDataOnePrivate,
                templ.getField<Any>(62).type,
                isoData.managementDataOnePrivate!!.length)
        } else {
            message.setValue(
                62,
                managementDataOne.toString(),
                templ.getField<Any>(62).type,
                managementDataOne.toString().length)
        }
        /** Set Primary Message Hash **/
        if (!StringUtil.isEmpty(terminalSessionKey)) {
            message.setValue(
                64,
                String(byteArrayOf(0x0)),
                templ.getField<Any>(64).type,
                templ.getField<Any>(64).length
            )
            val bytes = message.writeData()
            val length = bytes.size
            val temp = ByteArray(length - 64)
            if (length >= 64) {
                System.arraycopy(bytes, 0, temp, 0, length - 64)
            }
            val hashValue: String = KeyUtil().getMac(terminalSessionKey, temp)
            message.setValue(
                64,
                hashValue,
                templ.getField<Any>(64).type,
                templ.getField<Any>(64).length
            )
        } else if (isoData.primaryMessageHashValue != null && !StringUtil.isEmpty(isoData.primaryMessageHashValue)){
            message.setValue(
                64,
                isoData.primaryMessageHashValue,
                templ.getField<Any>(64).type,
                templ.getField<Any>(64).length
            )
        }
        return ISOMessage(message)
    }

}