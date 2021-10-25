package com.subsidian.emvcardsdkdemo.utils.transaction

import com.subsidian.emvcardmanager.builders.ISODataBuilder
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.enums.ISOProcCode

object DailyReportRequest {

    private val transactionUtilities = TransactionUtilities()

    fun build (): ISOData {
        return ISODataBuilder.Builder()
            .processingCode(ISOProcCode.DAILY_REPORT_DOWNLOAD_ISO_PROC_CODE.value)
            .transmissionDateTime(transactionUtilities.transmissionDateTime())
            .systemTraceAuditNumber(transactionUtilities.systemTraceAuditNumber())
            .localTime(transactionUtilities.localTime())
            .localDate(transactionUtilities.localDate())
            .cardAcceptorTerminalId(transactionUtilities.cardAcceptorTerminalId())
            .primaryMessageHashValue(transactionUtilities.primaryMessageHashValue())
            .build()
    }

}