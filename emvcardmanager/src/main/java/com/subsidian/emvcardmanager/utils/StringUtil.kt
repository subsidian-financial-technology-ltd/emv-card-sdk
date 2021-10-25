package com.subsidian.emvcardmanager.utils

import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and

object StringUtil {

    private val hexArray = "0123456789ABCDEF".toCharArray()

    fun fromByteArray(data: ByteArray): String {
        val hexChars = CharArray(data.size * 2)
        for (j in data.indices) {
            val v: Int = (data[j] and (0xFF).toByte()).toInt()
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    fun asciiFromByteArray(data: ByteArray): String {
        return hexToAscii(fromByteArray(data))
    }

    //it's come from http://www.baeldung.com/java-convert-hex-to-ascii
    fun asciiToHex(asciiStr: String): String {
        val chars = asciiStr.toCharArray()
        val hex = StringBuilder()
        for (ch in chars) {
            hex.append(Integer.toHexString(ch.toInt()))
        }
        return hex.toString()
    }

    //it's come from http://www.baeldung.com/java-convert-hex-to-ascii
    fun hexToAscii(hexStr: String): String {
        val output = StringBuilder("")
        var i = 0
        while (i < hexStr.length) {
            val str = hexStr.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output.toString()
    }

    fun asciiToHex(data: ByteArray): ByteArray {
        var hexChars: CharArray? = CharArray(data.size * 2)
        for (j in data.indices) {
            val v: Int = (data[j] and (0xFF).toByte()).toInt()
            hexChars!![j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        val res = ByteArray(hexChars!!.size)
        for (i in hexChars.indices) {
            res[i] = hexChars[i].toByte()
        }
        Arrays.fill(hexChars, '\u0000')
        hexChars = null
        return res
    }

    fun hexStringToByteArray(s: String): ByteArray {
        var s = s
        var len = s.length
        var padd = false
        if (len % 2 != 0) {
            s = "0$s"
            len++
            padd = true
        }
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                    + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    fun fromByteBuffer(readBuffer: ByteBuffer): String {
        return fromByteArray(Arrays.copyOfRange(readBuffer.array(), 0, readBuffer.position()))
    }

    fun intToHexString(value: Int): String {
        var hs = Integer.toHexString(value)
        if (hs.length % 2 != 0) hs = "0$hs"
        hs = hs.toUpperCase()
        return hs
    }

    fun asciiToByteArray(bytes: ByteArray): ByteArray {
        return hexStringToByteArray(hexToAscii(fromByteArray(bytes)))
    }

    fun toHexString(str: String): String {
        val sb = StringBuffer()
        for (i in 0 until str.length) {
            sb.append(toHexString(str[i]))
        }
        return sb.toString()
    }

    /**
     * convert into Hexadecimal notation of Unicode.<br></br>
     * example)a?\u0061
     * @param ch
     * @return
     */
    fun toHexString(ch: Char): String {
        var hex = Integer.toHexString(ch.toInt())
        while (hex.length < 4) {
            hex = "0$hex"
        }
        hex = "\\u$hex"
        return hex
    }

    /**
     * str fill，totalLength。
     *
     * @param fill
     * @param totalLength
     * @param str
     * @return
     */
    fun leftPadding(fill: Char, totalLength: Int, str: String): String {
        val buffer = StringBuffer()
        for (i in str.length until totalLength) {
            buffer.append(fill)
        }
        buffer.append(str)
        return buffer.toString()
    }

    fun leftPadding(fill: String?, totalLength: Int, str: String): String {
        val buffer = StringBuffer()
        for (i in str.length until totalLength) {
            buffer.append(fill)
        }
        buffer.append(str)
        return buffer.toString()
    }

    fun leftAppend(fill: String?, appendLength: Int, str: String?): String {
        val buffer = StringBuffer()
        for (i in 0 until appendLength) {
            buffer.append(fill)
        }
        buffer.append(str)
        return buffer.toString()
    }

    fun leftPad(pad: Char = '0', len: Int, str: String): String {
        if (str == null) return ""
        val sb = StringBuilder()
        while (sb.length + str.length < len) {
            sb.append(pad)
        }
        sb.append(str)
        return sb.toString()
    }

    fun rightAppend(fill: String?, appendLength: Int, str: String?): String {
        val buffer = StringBuilder(str)
        for (i in 0 until appendLength) {
            buffer.append(fill)
        }
        return buffer.toString()
    }

    fun rightPadding(fill: String?, totalLength: Int, str: String): String {
        val buffer = StringBuilder(str)
        while (str.length < totalLength) {
            buffer.append(fill)
        }
        return buffer.toString()
    }

    fun rightPad(pad: Char, len: Int, str: String): String {
        if (str == null) return ""
        val sb = StringBuilder()
        sb.append(str)
        while (sb.length < len) {
            sb.append(pad)
        }
        return sb.toString()
    }

    fun isEmpty(msg: String?): Boolean {
        return !(msg != null && "" != msg)
    }
}