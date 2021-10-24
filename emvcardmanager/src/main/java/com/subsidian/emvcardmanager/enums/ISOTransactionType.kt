package com.subsidian.emvcardmanager.enums

enum class ISOTransactionType(val value: String) {
    PURCHASE_TRANSACTION_TYPE("00"),
    BALANCE_TRANSACTION_TYPE("31"),
    REFUND_TRANSACTION_TYPE("20"),
    CASH_BACK_TRANSACTION_TYPE("09"),
    CASH_ADVANCE_TRANSACTION_TYPE("01"),
    PRE_AUTH_TRANSACTION_TYPE("60"),
    PRE_AUTH_COMPLETION_TRANSACTION_TYPE("61"),
    CASH_TRANSACTION_TRANSACTION_TYPE("42"),
    SETTLEMENT_TRANSACTION_TYPE("92"),
    CARD_VERIFICATION_TRANSACTION_TYPE("15"),
    SETTLEMENT_BATCH_UPLOAD_CLOSE_TRANSACTION_TYPE("96"),
}