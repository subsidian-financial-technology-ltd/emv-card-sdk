package com.subsidian.emvcardmanager.security

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*

class ValueGenerator {

    private var previousTimeMillis = System.currentTimeMillis()
    private var counter = 0L

    fun generateNextID(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        counter = if (currentTimeMillis == previousTimeMillis) counter + 1L and 1048575L else 0L
        previousTimeMillis = currentTimeMillis
        val timeComponent = currentTimeMillis and 8796093022207L shl 20
        return timeComponent or counter
    }

    fun generateReceiptID(): String{
        val uuid = UUID.randomUUID()
        val l: Long = ByteBuffer.wrap(uuid.toString().toByteArray()).long
        return l.toString(Character.MAX_RADIX)
    }

    fun generateCode(length: Int): Long {
        val ranGen = SecureRandom()
        val code = StringBuilder()
        for (i in 0..length){
            code.append(ranGen.nextInt(9))
        }
        return code.toString().toLong()
    }

    fun originalTransactionIDGen(): String? {
        val uuid = UUID.randomUUID()
        val randomUUIDString = uuid.toString()
        return java.lang.StringBuilder().append("TERMINAL_TRANSACTION_ID=").append(randomUUIDString)
            .toString()
    }

}