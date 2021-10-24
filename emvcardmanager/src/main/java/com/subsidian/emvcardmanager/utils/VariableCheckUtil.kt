package com.subsidian.emvcardmanager.utils

import com.subsidian.emvcardmanager.builders.ISODataBuilder
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.entities.ISOMessage
import com.subsidian.emvcardmanager.enums.ISOMessageType

object VariableCheckUtil {

    private val responseMessages: Map<String, String> = hashMapOf(
        Pair("00", "Transaction Approved"),
        Pair("01", "Refer to card issuer"),
        Pair("02", "Refer to card issuer, special condition"),
        Pair("03", "Invalid merchant"),
        Pair("04", "Pick-up card"),
        Pair("05", "Do not honor"),
        Pair("06", "Error"),
        Pair("07", "Pick-up card, special condition"),
        Pair("08", "Honor with identification"),
        Pair("09", "Request in progress"),
        Pair("10", "Approved, partial"),
        Pair("11", "Approved, VIP"),
        Pair("12", "Invalid transaction"),
        Pair("13", "Invalid amount"),
        Pair("14", "Invalid card number"),
        Pair("15", "No such issuer"),
        Pair("16", "Approved, update track 3"),
        Pair("17", "Customer cancellation"),
        Pair("18", "Customer displuse"),
        Pair("19", "Re-enter transaction"),
        Pair("20", "Invalid response"),
        Pair("21", "No action taken"),
        Pair("22", "Suspected malfunction"),
        Pair("23", "Unacceptable transaction fee"),
        Pair("24", "File update not supported"),
        Pair("25", "Unable to locate record"),
        Pair("26", "Duplicate record"),
        Pair("27", "File update field edit error"),
        Pair("28", "File update file locked"),
        Pair("29", "File update failed"),
        Pair("30", "Format error"),
        Pair("31", "Bank not supported"),
        Pair("32", "Completed partially"),
        Pair("33", "Expired card, pick-up"),
        Pair("34", "Suspected fraud, pick-up"),
        Pair("35", "Contact acquirer, pick-up"),
        Pair("36", "Restricted card, pick-up"),
        Pair("37", "Call acquirer security, pick-up"),
        Pair("38", "PIN tries exceeded, pick-up"),
        Pair("39", "No credit account"),
        Pair("40", "Function not supported"),
        Pair("41", "Lost card, pick-up"),
        Pair("42", "No universal account"),
        Pair("43", "Stolen card, pick-up"),
        Pair("44", "No investment account"),
        Pair("45", "Account closed"),
        Pair("46", "Identification required"),
        Pair("47", "Identification cross-check required"),
        Pair("51", "Not sufficient funds"),
        Pair("52", "No check account"),
        Pair("53", "No savings account"),
        Pair("54", "Expired card"),
        Pair("55", "Incorrect PIN"),
        Pair("56", "No card record"),
        Pair("57", "Transaction not permitted to cardholder"),
        Pair("58", "Transaction not permitted on terminal"),
        Pair("59", "Suspected fraud"),
        Pair("60", "Contact acquirer"),
        Pair("61", "Exceeds withdrawal limit"),
        Pair("62", "Restricted card"),
        Pair("63", "Security violation"),
        Pair("64", "Original amount incorrect"),
        Pair("65", "Exceeds withdrawal frequency"),
        Pair("66", "Call acquirer security"),
        Pair("67", "Hard capture"),
        Pair("68", "Response received too late"),
        Pair("69", "Advice received too late"),
        Pair("90", "Cut-off in progress"),
        Pair("91", "Issuer or switch inoperative"),
        Pair("92", "Routing error"),
        Pair("93", "Violation of law"),
        Pair("94", "Duplicate transaction"),
        Pair("95", "Reconcile error"),
        Pair("96", "System malfunction"),
        Pair("98", "Exceeds cash limit"),
        Pair("A1", "ATC not incremented"),
        Pair("A2", "ATC limit exceeded"),
        Pair("A3", "ATC configuration error"),
        Pair("A4", "CVR check failure"),
        Pair("A5", "CVR configuration error"),
        Pair("A6", "TVR check failure"),
        Pair("A7", "TVR configuration error"),
        Pair("C0", "Unacceptable PIN"),
        Pair("C1", "PIN Change failed"),
        Pair("C2", "PIN Unblock failed"),
        Pair("D1", "MAC Error"),
        Pair("E1", "Prepay error")
    )

    fun isoMessageType(message: ISOMessage): String {
        val fieldThree = (message.getMessage().getField<Any>(3) ?: message.getMessageType()).toString()
        return if (!StringUtil.isEmpty(fieldThree)) {
            when {
                fieldThree.startsWith("9A") -> {
                    "Terminal Master Key"
                }
                fieldThree.startsWith("9B") -> {
                    "Terminal Session Key"
                }
                fieldThree.startsWith("9G") -> {
                    "Terminal Pin Key"
                }
                fieldThree.startsWith("9C") -> {
                    "Terminal Parameter"
                }
                fieldThree.startsWith("9E") -> {
                    "CA Public Key"
                }
                fieldThree.startsWith("9F") -> {
                    "Application AID"
                }
                fieldThree.startsWith("9D") -> {
                    "Call Home"
                }
                fieldThree.startsWith("9I") -> {
                    "IPEK Track Two"
                }
                fieldThree.startsWith("9J") -> {
                    "IPEK EMV"
                }
                fieldThree.startsWith("9H") -> {
                    "Daily Report"
                }
                fieldThree.startsWith("92") -> {
                    "Settlement"
                }
                fieldThree.startsWith("96") -> {
                    "Settlement Batch"
                }
                fieldThree.startsWith("00") -> {
                    "Purchase"
                }
                fieldThree.startsWith("01") -> {
                    "Cash Advance"
                }
                fieldThree.startsWith("20") -> {
                    "Refund"
                }
                fieldThree.toInt(16) == ISOMessageType._0810.value.toInt(16) -> {
                    "Network Call"
                }
                else -> {
                    ""
                }
            }
        } else {
            "Unknown"
        }
    }

    fun isoFieldName(field: Int): String {
        return when(field) {
            2 -> "Primary account number - Mandatory"
            3 -> "Processing code - Mandatory"
            4 -> "Amount, transaction - Mandatory"
            7 -> "Transmission date and time - Conditional"
            9 -> "Conversion rate, settlement - Conditional"
            11 -> "Systems trace audit number - Conditional"
            12 -> "Time, local transaction - Mandatory"
            13 -> "Date, local transaction - Conditional"
            14 -> "Date, expiration - Conditional"
            15 -> "Date, Settlement - Conditional"
            16 -> "Date, conversion - Conditional"
            18 -> "Merchant’s type - Mandatory"
            22 -> "POS entry mode - Mandatory"
            23 -> "Card sequence number - Conditional"
            25 -> "POS condition code - Mandatory"
            26 -> "POS PIN capture code - Conditional"
            28 -> "Amount, transaction fee - Conditional"
            29 -> "Amount, settlement fee - Conditional"
            30 -> "Amount, transaction processing fee - Conditional"
            31 -> "Amount, settle processing fee - Conditional"
            32 -> "Acquiring institution id code - Mandatory"
            33 -> "Forwarding institution id code - Conditional"
            35 -> "Track 2 data - Conditional"
            37 -> "Retrieval reference number - Mandatory"
            38 -> "Authorization id response - Conditional"
            39 -> "Response code - Mandatory"
            40 -> "Service restriction code - Conditional"
            41 -> "Card acceptor terminal id - Optional"
            42 -> "Card acceptor id code - Conditional"
            43 -> "Card acceptor name/location - Conditional"
            44 -> "Additional response data - Optional"
            48 -> "Additional data - Conditional"
            49 -> "Currency code, transaction - Mandatory"
            50 -> "Currency code, settlement - Conditional"
            52 -> "PIN data - Conditional"
            53 -> "Security related control information - Conditional"
            54 -> "Additional amounts - Conditional"
            55 -> "Integrated Circuit Card System Related Data - Conditional"
            56 -> "Message reason code - Optional"
            58 -> "Authorizing agent id code - Conditional"
            59 -> "Transport (echo) data - Conditional"
            60 -> "Payment Information - Conditional"
            62 -> "Private, management data 1 - Conditional"
            63 -> "Private, management data 2 - Conditional"
            64 -> "Primary Message Hash Value - Conditional"
            67 -> "Extended payment code - Conditional"
            90 -> "Original data elements - Mandatory"
            95 -> "Replacement Amounts - Mandatory"
            98 -> "Payee - Conditional"
            100 -> "Receiving Institution ID Code - Optional"
            102 -> "Account identification 1 - Optional"
            103 -> "Account identification 2 - Optional"
            123 -> "POS data code - Mandatory"
            124 -> "Near Field Communication Data - Conditional"
            128 -> "Secondary Message Hash Value - Mandatory"
            else -> "Unknown Field"
        }
    }

    fun unpackISOMessage(message: ISOMessage): ISOData {
        val isoDataBuilder: ISODataBuilder.Builder = ISODataBuilder.Builder()
        for (i in 0..128) {
            when (i) {
                2 -> if (message.getMessage().hasField(i)) {
                        isoDataBuilder.primaryAccountNumber(message.getMessage().getField<Any>(i).toString())
                    }
                3 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.processingCode(message.getMessage().getField<Any>(i).toString())
                }
                4 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transactionAmount(message.getMessage().getField<Any>(i).toString())
                }
                7 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transmissionDateTime(message.getMessage().getField<Any>(i).toString())
                }
                9 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.settlementConversionRate(message.getMessage().getField<Any>(i).toString())
                }
                11 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.systemTraceAuditNumber(message.getMessage().getField<Any>(i).toString())
                }
                12 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.localTime(message.getMessage().getField<Any>(i).toString())
                }
                13 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.localDate(message.getMessage().getField<Any>(i).toString())
                }
                14 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.expirationDate(message.getMessage().getField<Any>(i).toString())
                }
                15 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.settlementDate(message.getMessage().getField<Any>(i).toString())
                }
                16 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.conversionDate(message.getMessage().getField<Any>(i).toString())
                }
                18 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.merchantType(message.getMessage().getField<Any>(i).toString())
                }
                22 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.posEntryMode(message.getMessage().getField<Any>(i).toString())
                }
                23 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.cardSequenceNumber(message.getMessage().getField<Any>(i).toString())
                }
                25 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.posConditionCode(message.getMessage().getField<Any>(i).toString())
                }
                26 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.posPinCaptureCode(message.getMessage().getField<Any>(i).toString())
                }
                28 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transactionFeeAmount(message.getMessage().getField<Any>(i).toString())
                }
                29 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.settlementAmount(message.getMessage().getField<Any>(i).toString())
                }
                30 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transactionProcessingFeeAmount(message.getMessage().getField<Any>(i).toString())
                }
                31 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.settleProcessingFeeAmount(message.getMessage().getField<Any>(i).toString())
                }
                32 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.acquiringInstitutionId(message.getMessage().getField<Any>(i).toString())
                }
                33 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.forwardingInstitutionId(message.getMessage().getField<Any>(i).toString())
                }
                35 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.trackTwoData(message.getMessage().getField<Any>(i).toString())
                }
                37 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.retrievalReferenceNumber(message.getMessage().getField<Any>(i).toString())
                }
                38 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.authorizationIdResponse(message.getMessage().getField<Any>(i).toString())
                }
                39 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.responseCode(message.getMessage().getField<Any>(i).toString())
                }
                40 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.serviceRestrictionCode(message.getMessage().getField<Any>(i).toString())
                }
                41 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.cardAcceptorTerminalId(message.getMessage().getField<Any>(i).toString())
                }
                42 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.cardAcceptorIdCode(message.getMessage().getField<Any>(i).toString())
                }
                43 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.cardAcceptorNameLocation(message.getMessage().getField<Any>(i).toString())
                }
                44 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.additionalResponseData(message.getMessage().getField<Any>(i).toString())
                }
                48 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.additionalData(message.getMessage().getField<Any>(i).toString())
                }
                49 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transactionCurrencyCode(message.getMessage().getField<Any>(i).toString())
                }
                50 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.settlementCurrencyCode(message.getMessage().getField<Any>(i).toString())
                }
                52 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.pinData(message.getMessage().getField<Any>(i).toString())
                }
                53 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.securityRelatedControlInformation(message.getMessage().getField<Any>(i).toString())
                }
                54 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.additionalAmounts(message.getMessage().getField<Any>(i).toString())
                }
                55 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.integratedCircuitCardData(message.getMessage().getField<Any>(i).toString())
                }
                56 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.messageReasonCode(message.getMessage().getField<Any>(i).toString())
                }
                58 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.authorizingAgentId(message.getMessage().getField<Any>(i).toString())
                }
                59 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.transportEchoData(message.getMessage().getField<Any>(i).toString())
                }
                60 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.paymentInformation(message.getMessage().getField<Any>(i).toString())
                }
                62 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.managementDataOnePrivate(message.getMessage().getField<Any>(i).toString())
                }
                63 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.managementDataTwoPrivate(message.getMessage().getField<Any>(i).toString())
                }
                64 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.primaryMessageHashValue(message.getMessage().getField<Any>(i).toString())
                }
                67 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.extendedPaymentCode(message.getMessage().getField<Any>(i).toString())
                }
                90 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.originalDataElement(message.getMessage().getField<Any>(i).toString())
                }
                95 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.replacementAmount(message.getMessage().getField<Any>(i).toString())
                }
                98 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.payee(message.getMessage().getField<Any>(i).toString())
                }
                100 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.receivingInstitutionId(message.getMessage().getField<Any>(i).toString())
                }
                102 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.accountIdentification1(message.getMessage().getField<Any>(i).toString())
                }
                103 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.accountIdentification2(message.getMessage().getField<Any>(i).toString())
                }
                123 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.posDataCode(message.getMessage().getField<Any>(i).toString())
                }
                124 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.nearFieldCommunicationData(message.getMessage().getField<Any>(i).toString())
                }
                128 -> if (message.getMessage().hasField(i)) {
                    isoDataBuilder.secondaryMessageHashValue(message.getMessage().getField<Any>(i).toString())
                }
            }
        }
        return isoDataBuilder.build()
    }

    fun getResponseCodeLabel(responseCode: String) : String {
        return if (responseMessages.containsKey(responseCode))
            responseMessages[responseCode]!!
        else
            "Unknown response code"
    }

}