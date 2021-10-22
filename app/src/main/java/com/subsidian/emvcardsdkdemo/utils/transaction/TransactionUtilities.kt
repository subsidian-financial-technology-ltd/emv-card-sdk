package com.subsidian.emvcardsdkdemo.utils.transaction

import com.subsidian.emvcardmanager.security.ValueGenerator
import com.subsidian.emvcardmanager.utils.StringUtil
import com.subsidian.emvcardmanager.utils.TimeUtil
import java.util.*

class TransactionUtilities {

    private val timeUtil = TimeUtil()
    private val now = Date(System.currentTimeMillis())

    fun transmissionDateTime() = timeUtil.getDateTimeMMddhhmmss(now)

    fun stan() = StringUtil.leftPadding('0', 6, ValueGenerator().generateCode(5).toString())

    fun localTime() = timeUtil.getTimehhmmss(now)

    fun localDate() = timeUtil.getDateMMdd(now)

    fun terminalId() = "20584535"
}