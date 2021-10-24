package com.subsidian.emvcardmanager.builders.transactions

import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.solab.iso8583.util.HexCodec
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.entities.ISOMessage
import com.subsidian.emvcardmanager.enums.ISOMessageType
import com.subsidian.emvcardmanager.enums.ISOTransactionType
import com.subsidian.emvcardmanager.security.ValueGenerator
import com.subsidian.emvcardmanager.utils.KeyUtil
import com.subsidian.emvcardmanager.utils.StringUtil

object FinancialTransactionRequestBuilder {

    /**
     * build purchase transaction message
     * With the hash passed in with the isoData of have the hash calculated by the SDK when a
     * terminal session key is provided.
     */
    fun build (isoData: ISOData, messageFactory: MessageFactory<IsoMessage>, terminalSessionKey: String = ""): ISOMessage {
        val type: Int = ISOMessageType._0200.value.toInt(16)
        val message: IsoMessage = messageFactory.newMessage(type)
        val templ: IsoMessage = messageFactory.getMessageTemplate(type)
        /** Set PAN **/
        if (isoData.primaryAccountNumber != null && !StringUtil.isEmpty(isoData.primaryAccountNumber)) {
            message.setValue(
                2,
                isoData.primaryAccountNumber,
                templ.getField<Any>(2).type,
                isoData.primaryAccountNumber!!.length
            )
        }
        /** Set ProcCode **/
        if (isoData.processingCode != null && !StringUtil.isEmpty(isoData.processingCode)) {
            message.setValue(
                3,
                isoData.processingCode,
                templ.getField<Any>(3).type,
                templ.getField<Any>(3).length
            )
        }
        /** Set Transaction Amount **/
        if (isoData.transactionAmount != null && !StringUtil.isEmpty(isoData.transactionAmount)) {
            message.setValue(
                4,
                isoData.transactionAmount,
                templ.getField<Any>(4).type,
                templ.getField<Any>(4).length
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
        /** Set Expiry Date **/
        if (isoData.expirationDate != null && !StringUtil.isEmpty(isoData.expirationDate)) {
            message.setValue(
                14,
                isoData.expirationDate,
                templ.getField<Any>(14).type,
                templ.getField<Any>(14).length
            )
        }
        /** Set Merchant Type **/
        if (isoData.merchantType != null && !StringUtil.isEmpty(isoData.merchantType)) {
            message.setValue(
                18,
                isoData.merchantType,
                templ.getField<Any>(18).type,
                templ.getField<Any>(18).length
            )
        }
        /** Set POS Entry Mode **/
        if (isoData.posEntryMode != null && !StringUtil.isEmpty(isoData.posEntryMode)) {
            message.setValue(
                22,
                isoData.posEntryMode,
                templ.getField<Any>(22).type,
                templ.getField<Any>(22).length
            )
        }
        /** Set Card Sequence Code **/
        if (isoData.cardSequenceNumber != null && !StringUtil.isEmpty(isoData.cardSequenceNumber)) {
            message.setValue(
                23,
                isoData.cardSequenceNumber,
                templ.getField<Any>(23).type,
                templ.getField<Any>(23).length
            )
        }
        /** Set POS Condition Code **/
        if (isoData.posConditionCode != null && !StringUtil.isEmpty(isoData.posConditionCode)) {
            message.setValue(
                25,
                isoData.posConditionCode,
                templ.getField<Any>(25).type,
                templ.getField<Any>(25).length
            )
        }
        /** Set POS Pin Capture Code **/
        if (isoData.posPinCaptureCode != null && !StringUtil.isEmpty(isoData.posPinCaptureCode)) {
            message.setValue(
                26,
                isoData.posPinCaptureCode,
                templ.getField<Any>(26).type,
                templ.getField<Any>(26).length
            )
        }
        /** Set Surcharge or Transaction Fee Amount **/
        if (isoData.transactionFeeAmount != null && !StringUtil.isEmpty(isoData.transactionFeeAmount)) {
            message.setValue(
                28,
                isoData.transactionFeeAmount,
                templ.getField<Any>(28).type,
                templ.getField<Any>(28).length
            )
        }
        /** Set Acquirer Institution ID **/
        if (isoData.acquiringInstitutionId != null && !StringUtil.isEmpty(isoData.acquiringInstitutionId)) {
            message.setValue(
                32,
                isoData.acquiringInstitutionId,
                templ.getField<Any>(32).type,
                isoData.acquiringInstitutionId!!.length
            )
        }
        /** Set Forwording Institution ID **/
        if (isoData.forwardingInstitutionId != null && !StringUtil.isEmpty(isoData.forwardingInstitutionId)) {
            message.setValue(
                33,
                isoData.forwardingInstitutionId,
                templ.getField<Any>(33).type,
                isoData.forwardingInstitutionId!!.length
            )
        }
        /** Set Track Two Data **/
        if (isoData.trackTwoData != null && !StringUtil.isEmpty(isoData.trackTwoData)) {
            message.setValue(
                35,
                isoData.trackTwoData,
                templ.getField<Any>(35).type,
                isoData.trackTwoData!!.length)
        }
        /** Set Reference Number or Retrieval Reference Number **/
        if (isoData.retrievalReferenceNumber != null && !StringUtil.isEmpty(isoData.retrievalReferenceNumber)) {
            message.setValue(
                37,
                isoData.retrievalReferenceNumber,
                templ.getField<Any>(37).type,
                templ.getField<Any>(37).length
            )
        }
        /** FOR REFUND **/
        /** Set Authentication Code **/
        if (isoData.authorizationIdResponse != null && (!StringUtil.isEmpty(isoData.authorizationIdResponse) && !isoData.processingCode!!
                .startsWith(ISOTransactionType.REFUND_TRANSACTION_TYPE.value))) {
            message.setValue(
                38,
                isoData.authorizationIdResponse,
                templ.getField<Any>(38).type,
                templ.getField<Any>(38).length
            )
        }
        /** Set Service Restriction Code **/
        if (isoData.serviceRestrictionCode != null && (!StringUtil.isEmpty(isoData.serviceRestrictionCode) && isoData.serviceRestrictionCode!!.length == 3)) {
            message.setValue(
                40,
                isoData.serviceRestrictionCode,
                templ.getField<Any>(40).type,
                templ.getField<Any>(40).length
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
        /** Set Card Acceptor Code or Merchant ID **/
        if (isoData.cardAcceptorIdCode != null && !StringUtil.isEmpty(isoData.cardAcceptorIdCode)) {
            message.setValue(
                42,
                isoData.cardAcceptorIdCode,
                templ.getField<Any>(42).type,
                templ.getField<Any>(42).length
            )
        }
        /** Set Card Acceptor Name and Location or Location **/
        if (isoData.cardAcceptorNameLocation != null && !StringUtil.isEmpty(isoData.cardAcceptorNameLocation)) {
            message.setValue(
                43,
                isoData.cardAcceptorNameLocation,
                templ.getField<Any>(43).type,
                templ.getField<Any>(43).length
            )
        }
        /** Set Transaction Currency Code **/
        if (isoData.transactionCurrencyCode != null && !StringUtil.isEmpty(isoData.transactionCurrencyCode)) {
            message.setValue(
                49,
                isoData.transactionCurrencyCode,
                templ.getField<Any>(49).type,
                templ.getField<Any>(49).length
            )
        }
        /** Set PIN Data **/
        if (isoData.pinData != null && !StringUtil.isEmpty(isoData.pinData)) {
            val pinBytes = HexCodec.hexDecode(isoData.pinData)
            message.setValue(
                52,
                pinBytes,
                templ.getField<Any>(52).type,
                templ.getField<Any>(52).length
            )
        }
        /** FOR CASHBACK **/
        /** Set Additional Amounts **/
        if (isoData.additionalAmounts != null && !StringUtil.isEmpty(isoData.additionalAmounts)) {
            message.setValue(
                54,
                isoData.additionalAmounts,
                templ.getField<Any>(54).type,
                templ.getField<Any>(54).length
            )
        }
        /** Set ICC Data **/
        if (isoData.integratedCircuitCardData != null && !StringUtil.isEmpty(isoData.integratedCircuitCardData)) {
            message.setValue(
                55,
                isoData.integratedCircuitCardData,
                templ.getField<Any>(55).type,
                isoData.integratedCircuitCardData!!.length)
        }
        /** Set Echo **/
        message.setValue(
            59,
            ValueGenerator().originalTransactionIDGen(),
            templ.getField<Any>(59).type,
            templ.getField<Any>(59).length
        )
        /** FOR PRE AUTH COMPLETION **/
        if (isoData.replacementAmount != null &&
            isoData.originalDataElement != null &&
            isoData.processingCode!!.startsWith(ISOTransactionType.PRE_AUTH_COMPLETION_TRANSACTION_TYPE.value)) {
            /** Set Original Data Element **/
            message.setValue(
                90,
                isoData.originalDataElement,
                templ.getField<Any>(90).type,
                templ.getField<Any>(90).length)
            /** Set Additional Amounts **/
            message.setValue(
                95,
                isoData.replacementAmount,
                templ.getField<Any>(95).type,
                templ.getField<Any>(95).length)
        }
        /** Set POS Data Code **/
        if (isoData.posDataCode != null && !StringUtil.isEmpty(isoData.posDataCode)) {
            message.setValue(
                123,
                isoData.posDataCode,
                templ.getField<Any>(123).type,
                templ.getField<Any>(123).length
            )
        }
        /** Set Additional Amounts **/
        if (!StringUtil.isEmpty(terminalSessionKey)) {
            message.setValue(
                128,
                String(byteArrayOf(0x0)),
                templ.getField<Any>(128).type,
                templ.getField<Any>(128).length
            )
            val bytes = message.writeData()
            val length = bytes.size
            val temp = ByteArray(length - 64)
            if (length >= 64) {
                System.arraycopy(bytes, 0, temp, 0, length - 64)
            }
            val hashValue: String = KeyUtil().getMac(terminalSessionKey, temp) //SHA256
            message.setValue(
                128,
                hashValue,
                templ.getField<Any>(128).type,
                templ.getField<Any>(128).length
            )
        } else if (isoData.secondaryMessageHashValue != null && !StringUtil.isEmpty(isoData.secondaryMessageHashValue)){
            message.setValue(
                128,
                isoData.secondaryMessageHashValue,
                templ.getField<Any>(128).type,
                templ.getField<Any>(128).length
            )
        }
        return ISOMessage(message)
    }

}