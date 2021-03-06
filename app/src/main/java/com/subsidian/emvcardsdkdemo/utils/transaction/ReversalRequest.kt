package com.subsidian.emvcardsdkdemo.utils.transaction

import com.subsidian.emvcardmanager.builders.ISODataBuilder
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.enums.ISOAccountType
import com.subsidian.emvcardmanager.enums.ISOMessageType
import com.subsidian.emvcardmanager.enums.ISOTransactionType
import com.subsidian.emvcardmanager.security.ValueGenerator

object ReversalRequest {

    private val transactionUtilities = TransactionUtilities()

    fun build (): ISOData {
        val transactionType = ISOTransactionType.PURCHASE_TRANSACTION_TYPE
        val accountType = ISOAccountType.DEFAULT_ACCTOUNT_TYPE
        return ISODataBuilder.Builder()
            .messageType(ISOMessageType._0420.value) // set to 0421 if it's a repeat
            .primaryAccountNumber(transactionUtilities.primaryAccountNumber())
            .processingCode(transactionType.value.plus(accountType.value).plus("00"))
            .transactionAmount(transactionUtilities.transactionAmount())
            .transmissionDateTime(transactionUtilities.transmissionDateTime())
            .settlementConversionRate("")
            .systemTraceAuditNumber(transactionUtilities.systemTraceAuditNumber())
            .localTime(transactionUtilities.localTime())
            .localDate(transactionUtilities.localDate())
            .expirationDate(transactionUtilities.expirationDate())
            .settlementDate("")
            .conversionDate("")
            .merchantType(transactionUtilities.merchantType())
            .posEntryMode(transactionUtilities.posEntryMode())
            .cardSequenceNumber(transactionUtilities.cardSequenceNumber())
            .posConditionCode(transactionUtilities.posConditionCode())
            .posPinCaptureCode(transactionUtilities.posPinCaptureCode())
            .transactionFeeAmount(transactionUtilities.transactionFeeAmount())
            .settlementAmount("")
            .transactionProcessingFeeAmount("")
            .settleProcessingFeeAmount("")
            .acquiringInstitutionId(transactionUtilities.acquiringInstitutionId())
            .forwardingInstitutionId("")
            .trackTwoData(transactionUtilities.trackTwoData())
            .retrievalReferenceNumber(transactionUtilities.retrievalReferenceNumber())
            .authorizationIdResponse("")
            .responseCode("")
            .serviceRestrictionCode(transactionUtilities.serviceRestrictionCode())
            .cardAcceptorTerminalId(transactionUtilities.cardAcceptorTerminalId())
            .cardAcceptorIdCode(transactionUtilities.cardAcceptorIdCode())
            .cardAcceptorNameLocation(transactionUtilities.cardAcceptorNameLocation())
            .additionalResponseData("")
            .additionalData("")
            .transactionCurrencyCode(transactionUtilities.transactionCurrencyCode())
            .settlementCurrencyCode("")
            .pinData("")
            .securityRelatedControlInformation("")
            .additionalAmounts("")
            .integratedCircuitCardData("")
            .messageReasonCode("")
            .authorizingAgentId("")
            .transportEchoData("")
            .paymentInformation("")
            .managementDataOnePrivate("")
            .managementDataTwoPrivate("")
            .primaryMessageHashValue("")
            .extendedPaymentCode("")
            .originalDataElement(ValueGenerator()
                .reversalOriginalDataElement(
                    ISOMessageType._0200.value,
                    transactionUtilities.systemTraceAuditNumber(),
                    transactionUtilities.transmissionDateTime(),
                    transactionUtilities.acquiringInstitutionId(),
                    transactionUtilities.forwardingInstitutionId()
                ))
            .replacementAmount("")
            .payee("")
            .receivingInstitutionId("")
            .accountIdentification1("")
            .accountIdentification2("")
            .posDataCode(transactionUtilities.posDataCode())
            .nearFieldCommunicationData("")
            .secondaryMessageHashValue(transactionUtilities.secondaryMessageHashValue())
            .build()
    }

}