package com.subsidian.emvcardmanager.builders

import com.subsidian.emvcardmanager.entities.ISOData

class ISODataBuilder {

    data class Builder(
        var messageType: String? = null,
        var primaryAccountNumber: String? = null,
        var processingCode: String? = null,
        var transactionAmount: String? = null,
        var transmissionDateTime: String? = null,
        var settlementConversionRate: String? = null,
        var systemTraceAuditNumber: String? = null,
        var localTime: String? = null,
        var localDate: String? = null,
        var expirationDate: String? = null,
        var settlementDate: String? = null,
        var conversionDate: String? = null,
        var merchantType: String? = null,
        var posEntryMode: String? = null,
        var cardSequenceNumber: String? = null,
        var posConditionCode: String? = null,
        var posPinCaptureCode: String? = null,
        var transactionFeeAmount: String? = null,
        var settlementAmount: String? = null,
        var transactionProcessingFeeAmount: String? = null,
        var settleProcessingFeeAmount: String? = null,
        var acquiringInstitutionId: String? = null,
        var forwardingInstitutionId: String? = null,
        var trackTwoData: String? = null,
        var retrievalReferenceNumber: String? = null,
        var authorizationIdResponse: String? = null,
        var responseCode: String? = null,
        var serviceRestrictionCode: String? = null,
        var cardAcceptorTerminalId: String? = null,
        var cardAcceptorIdCode: String? = null,
        var cardAcceptorNameLocation: String? = null,
        var additionalResponseData: String? = null,
        var additionalData: String? = null,
        var transactionCurrencyCode: String? = null,
        var settlementCurrencyCode: String? = null,
        var pinData: String? = null,
        var securityRelatedControlInformation: String? = null,
        var additionalAmounts: String? = null,
        var integratedCircuitCardData: String? = null,
        var messageReasonCode: String? = null,
        var authorizingAgentId: String? = null,
        var transportEchoData: String? = null,
        var paymentInformation: String? = null,
        var managementDataOnePrivate: String? = null,
        var managementDataTwoPrivate: String? = null,
        var primaryMessageHashValue: String? = null,
        var extendedPaymentCode: String? = null,
        var originalDataElement: String? = null,
        var replacementAmount: String? = null,
        var payee: String? = null,
        var receivingInstitutionId: String? = null,
        var accountIdentification1: String? = null,
        var accountIdentification2: String? = null,
        var posDataCode: String? = null,
        var nearFieldCommunicationData: String? = null,
        var secondaryMessageHashValue: String? = null,
        ) {

        private val isoData: ISOData = ISOData()

        fun messageType(messageType: String) = apply { this.isoData.merchantType = merchantType }
        fun primaryAccountNumber(primaryAccountNumber: String) = apply { this.isoData.primaryAccountNumber = primaryAccountNumber }
        fun processingCode(processingCode: String) = apply { this.isoData.processingCode = processingCode }
        fun transactionAmount(transactionAmount: String) = apply { this.isoData.transactionAmount = transactionAmount }
        fun transmissionDateTime(transmissionDateTime: String) = apply { this.isoData.transmissionDateTime = transmissionDateTime }
        fun settlementConversionRate(settlementConversionRate: String) = apply { this.isoData.settlementConversionRate = settlementConversionRate }
        fun systemTraceAuditNumber(systemTraceAuditNumber: String) = apply { this.isoData.systemTraceAuditNumber = systemTraceAuditNumber }
        fun localTime(localTime: String) = apply { this.isoData.localTime = localTime }
        fun localDate(localDate: String) = apply { this.isoData.localDate = localDate }
        fun expirationDate(expirationDate: String) = apply { this.isoData.expirationDate = expirationDate }
        fun settlementDate(settlementDate: String) = apply { this.isoData.settlementDate = settlementDate }
        fun conversionDate(conversionDate: String) = apply { this.isoData.conversionDate = conversionDate }
        fun merchantType(merchantType: String) = apply { this.isoData.merchantType = merchantType }
        fun posEntryMode(posEntryMode: String) = apply { this.isoData.posEntryMode = posEntryMode }
        fun cardSequenceNumber(cardSequenceNumber: String) = apply { this.isoData.cardSequenceNumber = cardSequenceNumber }
        fun posConditionCode(posConditionCode: String) = apply { this.isoData.posConditionCode = posConditionCode }
        fun posPinCaptureCode(posPinCaptureCode: String) = apply { this.isoData.posPinCaptureCode = posPinCaptureCode }
        fun transactionFeeAmount(transactionFeeAmount: String) = apply { this.isoData.transactionFeeAmount = transactionFeeAmount }
        fun settlementAmount(settlementAmount: String) = apply { this.isoData.settlementAmount = settlementAmount }
        fun transactionProcessingFeeAmount(transactionProcessingFeeAmount: String) = apply { this.isoData.transactionProcessingFeeAmount = transactionProcessingFeeAmount }
        fun settleProcessingFeeAmount(settleProcessingFeeAmount: String) = apply { this.isoData.settleProcessingFeeAmount = settleProcessingFeeAmount }
        fun acquiringInstitutionId(acquiringInstitutionId: String) = apply { this.isoData.acquiringInstitutionId = acquiringInstitutionId }
        fun forwardingInstitutionId(forwardingInstitutionId: String) = apply { this.isoData.forwardingInstitutionId = forwardingInstitutionId }
        fun trackTwoData(trackTwoData: String) = apply { this.isoData.trackTwoData = trackTwoData }
        fun retrievalReferenceNumber(retrievalReferenceNumber: String) = apply { this.isoData.retrievalReferenceNumber = retrievalReferenceNumber }
        fun authorizationIdResponse(authorizationIdResponse: String) = apply { this.isoData.authorizationIdResponse = authorizationIdResponse }
        fun responseCode(responseCode: String) = apply { this.isoData.responseCode = responseCode }
        fun serviceRestrictionCode(serviceRestrictionCode: String) = apply { this.isoData.serviceRestrictionCode = serviceRestrictionCode }
        fun cardAcceptorTerminalId(cardAcceptorTerminalId: String) = apply { this.isoData.cardAcceptorTerminalId = cardAcceptorTerminalId }
        fun cardAcceptorIdCode(cardAcceptorIdCode: String) = apply { this.isoData.cardAcceptorIdCode = cardAcceptorIdCode }
        fun cardAcceptorNameLocation(cardAcceptorNameLocation: String) = apply { this.isoData.cardAcceptorNameLocation = cardAcceptorNameLocation }
        fun additionalResponseData(additionalResponseData: String) = apply { this.isoData.additionalResponseData = additionalResponseData }
        fun additionalData(additionalData: String) = apply { this.isoData.additionalData = additionalData }
        fun transactionCurrencyCode(transactionCurrencyCode: String) = apply { this.isoData.transactionCurrencyCode = transactionCurrencyCode }
        fun settlementCurrencyCode(settlementCurrencyCode: String) = apply { this.isoData.settlementCurrencyCode = settlementCurrencyCode }
        fun pinData(pinData: String) = apply { this.isoData.pinData = pinData }
        fun securityRelatedControlInformation(securityRelatedControlInformation: String) = apply { this.isoData.securityRelatedControlInformation = securityRelatedControlInformation }
        fun additionalAmounts(additionalAmounts: String) = apply { this.isoData.additionalAmounts = additionalAmounts }
        fun integratedCircuitCardData(integratedCircuitCardData: String) = apply { this.isoData.integratedCircuitCardData = integratedCircuitCardData }
        fun messageReasonCode(messageReasonCode: String) = apply { this.isoData.messageReasonCode = messageReasonCode }
        fun authorizingAgentId(authorizingAgentId: String) = apply { this.isoData.authorizingAgentId = authorizingAgentId }
        fun transportEchoData(transportEchoData: String) = apply { this.isoData.transportEchoData = transportEchoData }
        fun paymentInformation(paymentInformation: String) = apply { this.isoData.paymentInformation = paymentInformation }
        fun managementDataOnePrivate(managementDataOnePrivate: String) = apply { this.isoData.managementDataOnePrivate = managementDataOnePrivate }
        fun managementDataTwoPrivate(managementDataTwoPrivate: String) = apply { this.isoData.managementDataTwoPrivate = managementDataTwoPrivate }
        fun primaryMessageHashValue(primaryMessageHashValue: String) = apply { this.isoData.primaryMessageHashValue = primaryMessageHashValue }
        fun extendedPaymentCode(extendedPaymentCode: String) = apply { this.isoData.extendedPaymentCode = extendedPaymentCode }
        fun originalDataElement(originalDataElement: String) = apply { this.isoData.originalDataElement = originalDataElement }
        fun replacementAmount(replacementAmount: String) = apply { this.isoData.replacementAmount = replacementAmount }
        fun payee(payee: String) = apply { this.isoData.payee = payee }
        fun receivingInstitutionId(receivingInstitutionId: String) = apply { this.isoData.receivingInstitutionId = receivingInstitutionId }
        fun accountIdentification1(accountIdentification1: String) = apply { this.isoData.accountIdentification1 = accountIdentification1 }
        fun accountIdentification2(accountIdentification2: String) = apply { this.isoData.accountIdentification2 = accountIdentification2 }
        fun posDataCode(posDataCode: String) = apply { this.isoData.posDataCode = posDataCode }
        fun nearFieldCommunicationData(nearFieldCommunicationData: String) = apply { this.isoData.nearFieldCommunicationData = nearFieldCommunicationData }
        fun secondaryMessageHashValue(secondaryMessageHashValue: String) = apply { this.isoData.secondaryMessageHashValue = secondaryMessageHashValue }
        fun build() = this.isoData
    }
}