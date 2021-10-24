package com.subsidian.emvcardmanager.enums

enum class ISOTransactionTypeName(val value: String) {
     PURCHASE("Purchase"),
     BALANCE("Balance"),
     REFUND("Refund"),
     CASH_ADVANCE("Cash Advance"),
     CASH_BACK("Cash Back"),
     PRE_AUTH("Pre-Authorization"),
     PRE_AUTH_COMPLETION("Pre-Authorization Completion"),
     CARD_VERIFICATION("Card Verification"),
     TRANSACTION("Transaction"),
     SETTLEMENT("Settlement"),
}