package com.subsidian.emvcardsdkdemo.utils.transaction

import com.subsidian.emvcardmanager.security.ValueGenerator
import com.subsidian.emvcardmanager.utils.StringUtil
import com.subsidian.emvcardmanager.utils.TimeUtil
import java.util.*

class TransactionUtilities {

    private val timeUtil = TimeUtil()
    private val now = Date(System.currentTimeMillis())

    fun primaryAccountNumber() = "4761739001010010"

    fun processingCode() = "000000"

    fun transactionAmount() = "000000000253"

    fun transmissionDateTime() = timeUtil.getDateTimeMMddhhmmss(now)

    fun settlementConversionRate() = ""

    fun systemTraceAuditNumber() = StringUtil.leftPadding('0', 6, ValueGenerator().generateCode(5).toString())

    fun localTime() = timeUtil.getTimehhmmss(now)

    fun localDate() = timeUtil.getDateMMdd(now)

    fun expirationDate() = "2212"

    fun settlementDate() = ""

    fun conversionDate() = ""

    fun merchantType() = "1520"

    fun posEntryMode() = "071"

    fun cardSequenceNumber() = "001"

    fun posConditionCode() = "00"

    fun posPinCaptureCode() = "04"

    fun transactionFeeAmount() = "C00000000"

    fun settlementAmount() = ""

    fun transactionProcessingFeeAmount() = ""

    fun settleProcessingFeeAmount() = ""

    fun acquiringInstitutionId() = "00000000000"

    fun forwardingInstitutionId() = ""

    fun trackTwoData() = "4761739001010010D22122011143844489"

    fun retrievalReferenceNumber() = StringUtil.leftPadding('0', 12, ValueGenerator().generateCode(12).toString())

    fun authorizationIdResponse() = ""

    fun responseCode() = ""

    fun serviceRestrictionCode() = "201"

    fun cardAcceptorTerminalId() = "20584535"

    fun cardAcceptorIdCode() = "2044LA000017579"

    fun cardAcceptorNameLocation() = "Office                 LA           LANG"

    fun additionalResponseData() = ""

    fun additionalData() = ""

    fun transactionCurrencyCode() = "566"

    fun settlementCurrencyCode() = ""

    fun pinData() = ""

    fun securityRelatedControlInformation() = ""

    fun additionalAmounts() = ""

    fun integratedCircuitCardData() = "9F260854F3AF21B4227A379F2701009F100706010A038000009F37040B859F619F3602070E950500000000009A032106109C01209F0201305F2A03353636820220009F1A033536369F03060000000000009F3303E090C89F350057114761739001010010D221220111438444895A0847617390010100105F3401018407A00000000310105F2015564953412044454249542F43415244484F4C444552"

    fun messageResponseCode() = ""

    fun authorizingAgentId() = ""

    fun transportEchoData() = ""

    fun paymentInformation() = ""

    fun managementDataOnePrivate() = ""

    fun managementDataTwoPrivate() = ""

    fun primaryMessageHashValue() = "B6423D83EDE27AE98A05D78063381AE3B895CB59BE7B3D0C3E17BD20BCAA668C"

    fun extendedPaymentCode() = ""

    fun originalDataElement() = ""

    fun replacementAmount() = ""

    fun payee() = ""

    fun receivingInstitutionId() = ""

    fun accountIdentification1() = ""

    fun accountIdentification2() = ""

    fun posDataCode() = "911101513344101"

    fun nearFieldCommunicationData() = ""

    fun secondaryMessageHashValue() = "B6423D83EDE27AE98A05D78063381AE3B895CB59BE7B3D0C3E17BD20BCAA668C"
}