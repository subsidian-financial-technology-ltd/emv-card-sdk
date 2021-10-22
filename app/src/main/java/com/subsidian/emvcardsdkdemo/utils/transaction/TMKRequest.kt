package com.subsidian.emvcardsdkdemo.utils.transaction

import com.subsidian.emvcardmanager.builders.ISODataBuilder
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.enums.ProcCode

object TMKRequest {

    private val transactionUtilities = TransactionUtilities()

    fun build (): ISOData {
        return ISODataBuilder.Builder()
            .processingCode(ProcCode.TMK_DOWNLOAD_PROC_CODE.value)
            .transmissionDateTime(transactionUtilities.transmissionDateTime())
            .systemTraceAuditNumber(transactionUtilities.stan())
            .localTime(transactionUtilities.localTime())
            .localDate(transactionUtilities.localDate())
            .cardAcceptorTerminalId(transactionUtilities.terminalId())
            .build()
    }

}