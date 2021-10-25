package com.subsidian.emvcardmanager.utils

import com.subsidian.emvcardmanager.utils.ByteUtil
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.jvm.JvmOverloads

object ByteUtil {

    private val LOG = LoggerFactory.getLogger(ByteUtil::class.java)
    private const val TAG = "[ByteUtil]"
    private val CS = "0123456789ABCDEF".toCharArray()
    fun hex2byte(hex: Char): Byte {
        if (hex in 'a'..'f') {
            return (hex - 'a' + 10).toByte()
        }
        if (hex in 'A'..'F') {
            return (hex - 'A' + 10).toByte()
        }
        return if (hex in '0'..'9') {
            (hex - '0').toByte()
        } else 0
    }

    fun ascii2Bcd(ascii: String?): ByteArray? {
        var ascii = ascii
        if (ascii == null) return null
        if (ascii.length and 0x01 == 1) ascii = "0$ascii"
        val asc = ascii.toByteArray()
        val bcd = ByteArray(ascii.length shr 1)
        for (i in bcd.indices) {
            bcd[i] = (((hex2byte(asc[2 * i].toChar())).toInt() shl 4).toByte() or hex2byte(
                asc[2 * i + 1].toChar()
            )) as Byte
        }
        return bcd
    }

    fun bytes2Int(data: ByteArray?): Int {
        if (data == null || data.size == 0) {
            return 0
        }
        var total = 0
        for (i in data.indices) {
            total += (data[i] and (0xff).toByte()).toInt() shl (data.size - i - 1) * 8
        }
        return total
    }

    /**
     * @param n:[0,65536]
     */
    fun intToHexString(n: Int): String {
        val b = ByteArray(2)
        b[0] = (n / 256).toByte()
        b[1] = (n % 256).toByte()
        return String.format("%02X%02X", b[0], b[1])
    }

    fun int3ToHexString(n: Int): String {
        val b = ByteArray(3)
        b[0] = (n / 256).toByte()
        b[1] = (n % 256).toByte()
        return String.format("%02X%02X", b[0], b[1])
    }

    //    public static String bytes2HexString(byte[] data) {
    //        if (data == null)
    //            return "";
    //        StringBuilder buffer = new StringBuilder();
    //        for (byte b : data) {
    //            String hex = Integer.toHexString(b & 0xff);
    //            if (hex.length() == 1) {
    //                buffer.append('0');
    //            }
    //            buffer.append(hex);
    //        }
    //        return buffer.toString().toUpperCase();
    //    }
    @JvmStatic
    fun bytes2HexString(data: ByteArray?): String {
        if (data == null) return ""
        val buffer = StringBuilder()
        for (b in data) {
            val hex = Integer.toHexString((b and (0xff).toByte()).toInt())
            if (hex.length == 1) {
                buffer.append('0')
            }
            buffer.append(hex)
        }
        return buffer.toString().toUpperCase()
    }

    @JvmStatic
    fun hexString2Bytes(data: String?): ByteArray? {
        var data = data
        if (data == null) return null
        val result = ByteArray((data.length + 1) / 2)
        if (data.length and 1 == 1) {
            data += "0"
        }
        for (i in result.indices) {
            result[i] = (hex2byte(data[i * 2 + 1]) or ((hex2byte(data[i * 2])).toInt() shl 4).toByte()) as Byte
        }
        return result
    }

    @JvmOverloads
    fun toBytes(data: String, charsetName: String? = "ISO-8859-1"): ByteArray? {
        return try {
            data.toByteArray(charset(charsetName!!))
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }

    fun toGBK(data: String): ByteArray? {
        return toBytes(data, "GBK")
    }

    fun toGB2312(data: String): ByteArray? {
        return toBytes(data, "GB2312")
    }

    fun toUtf8(data: String): ByteArray? {
        return toBytes(data, "UTF-8")
    }

    @JvmOverloads
    fun fromBytes(data: ByteArray?, charsetName: String? = "ISO-8859-1"): String? {
        return try {
            String(data!!, Charset.forName(charsetName))
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }

    fun fromGBK(data: ByteArray?): String? {
        return fromBytes(data, "GBK")
    }

    fun fromGB2312(data: ByteArray?): String? {
        return fromBytes(data, "GB2312")
    }

    fun fromGB2312New(data: String): String? {
        return fromGB2312(toBytes(data.trim { it <= ' ' }))
    }

    fun fromUtf8(data: ByteArray?): String? {
        return fromBytes(data, "UTF-8")
    }

    fun dumpHex(msg: String?, bytes: ByteArray) {
        var msg = msg
        val length = bytes.size
        msg = msg ?: ""
        val sb = StringBuilder()
        sb.append(
            String.format(
                "\n---------------------- $msg(len:%d) ----------------------\n",
                length
            )
        )
        for (i in bytes.indices) {
            if (i % 16 == 0) {
                if (i != 0) {
                    sb.append('\n')
                }
                sb.append(String.format("0x%08X    ", i))
            }
            sb.append(String.format("%02X ", bytes[i]))
        }
        sb.append("\n----------------------------------------------------------------------\n")
        LOG.debug("[{}]: [{}]", TAG, sb.toString())
    }

    fun str2HexStr(str: String): String {
        val mChars = "0123456789ABCDEF".toCharArray()
        val sb = StringBuilder()
        val bs = str.toByteArray()
        for (i in bs.indices) {
            sb.append(mChars[(bs[i] and (0xFF).toByte()).toInt() shr 4])
            sb.append(mChars[(bs[i] and (0xFF).toByte()).toInt()])
        }
        return sb.toString().trim { it <= ' ' }
    }

    /**
     * 将整数按大端模式转为4字节数组
     *
     * @param intValue
     * @return 转换结果
     */
    fun intToBytes(intValue: Int): ByteArray {
        val bytes = ByteArray(4)
        for (i in bytes.indices) {
            bytes[i] = (intValue shr (3 - i shl 3) and 0xFF).toByte()
        }
        return bytes
    }

    /**
     * 将整数按小端模式转为4字节数组
     *
     * @param value
     * @return 转换结果
     */
    fun int2Bytes(value: Int): ByteArray {
        val src = ByteArray(4)
        src[3] = (value shr 24 and 0xFF).toByte()
        src[2] = (value shr 16 and 0xFF).toByte()
        src[1] = (value shr 8 and 0xFF).toByte()
        src[0] = (value and 0xFF).toByte()
        return src
    }

    // ASCII的字符串转byte数组
    fun ascii2Bytes(asciiString: String?): ByteArray? {
        return asciiString?.toByteArray(StandardCharsets.US_ASCII)
    }

    // ASCII的字节数组串转byte数组
    fun ASCII_To_BCD(ascii: ByteArray, asc_len: Int): ByteArray {
        val bcd = ByteArray(asc_len / 2)
        var j = 0
        for (i in 0 until (asc_len + 1) / 2) {
            bcd[i] = asc_to_bcd(ascii[j++])
            bcd[i] = ((if (j >= asc_len) 0x00 else asc_to_bcd(ascii[j++])) + (bcd[i].toInt() shl 4)) as Byte
        }
        return bcd
    }

    private fun asc_to_bcd(asc: Byte): Byte {
        val bcd: Byte
        bcd =
            if (asc >= '0'.toByte() && asc <= '9'.toByte()) (asc - '0'.toByte()).toByte() else if (asc >= 'A'.toByte() && asc <= 'F'.toByte()) (asc - 'A'.toByte() + 10).toByte() else if (asc >= 'a'.toByte() && asc <= 'f'.toByte()) (asc - 'a'.toByte() + 10).toByte() else (asc - 48).toByte()
        return bcd
    }

    fun subBytes(data: ByteArray, offset: Int, len: Int): ByteArray? {
        var len = len
        if (offset < 0 || data.size <= offset) {
            return null
        }
        if (len < 0 || data.size < offset + len) {
            len = data.size - offset
        }
        val ret = ByteArray(len)
        System.arraycopy(data, offset, ret, 0, len)
        return ret
    }

    fun merage(data: Array<ByteArray?>): ByteArray {
        var len = 0
        for (i in data.indices) {
            requireNotNull(data[i]) { "" }
            len += data[i]?.size ?: 0
        }
        val newData = ByteArray(len)
        len = 0
        val j = data.size
        for (i in 0 until j) {
            val d = data[i]
            System.arraycopy(d, 0, newData, len, d!!.size)
            len += d.size
        }
        return newData
    }

    @JvmStatic
    fun hexToBytes(s: String): ByteArray {
        var s = s
        s = s.toUpperCase()
        val len = s.length / 2
        var ii = 0
        val bs = ByteArray(len)
        for (i in 0 until len) {
            var c = s[ii++]
            var h: Int
            h = if (c <= '9') {
                c.toInt() - 48
            } else {
                c.toInt() - 65 + 10
            }
            h = h shl 4
            c = s[ii++]
            h = if (c <= '9') {
                h or c.toInt() - 48
            } else {
                h or c.toInt() - 65 + 10
            }
            bs[i] = h.toByte()
        }
        return bs
    }

    fun bytesToHex(bs: ByteArray): String {
        val cs = CharArray(bs.size * 2)
        var io = 0
        val var5 = bs.size
        for (var4 in 0 until var5) {
            val n = bs[var4]
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
        }
        return String(cs)
    }

    fun bytesToHex(bs: ByteArray, len: Int): String {
        val cs = CharArray(len * 2)
        var io = 0
        for (i in 0 until len) {
            val n = bs[i]
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
        }
        return String(cs)
    }

    fun bytesToHex(bs: ByteArray, pos: Int, len: Int): String {
        val cs = CharArray(len * 2)
        var io = 0
        for (i in pos until pos + len) {
            val n = bs[i]
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
        }
        return String(cs)
    }

    fun bytesToHex(bs: ByteArray, gap: Char): String {
        val cs = CharArray(bs.size * 3)
        var io = 0
        val var6 = bs.size
        for (var5 in 0 until var6) {
            val n = bs[var5]
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
            cs[io++] = gap
        }
        return String(cs)
    }

    fun bytesToHex(bs: ByteArray, gap: Char, len: Int): String {
        val cs = CharArray(len * 3)
        var io = 0
        for (i in 0 until len) {
            val n = bs[i]
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
            cs[io++] = gap
        }
        return String(cs)
    }

    fun bytesToCppHex(bs: ByteArray, bytePerLine: Int): String {
        var bytePerLine = bytePerLine
        if (bytePerLine <= 0 || bytePerLine >= 65536) {
            bytePerLine = 65536
        }
        var lines = 0
        if (bytePerLine < 65536) {
            lines = (bs.size + bytePerLine - 1) / bytePerLine
        }
        val cs = CharArray(bs.size * 5 + lines * 3)
        var io = 0
        var ic = 0
        val var8 = bs.size
        for (var7 in 0 until var8) {
            val n = bs[var7]
            cs[io++] = '0'
            cs[io++] = 'x'
            cs[io++] = CS[(n).toInt() shr 4 and 15]
            cs[io++] = CS[(n).toInt() shr 0 and 15]
            cs[io++] = ','
            if (bytePerLine < 65536) {
                ++ic
                if (ic >= bytePerLine) {
                    ic = 0
                    cs[io++] = '/'
                    cs[io++] = '/'
                    cs[io++] = '\n'
                }
            }
        }
        if (bytePerLine < 65536 && io < cs.size) {
            cs[io++] = '/'
            cs[io++] = '/'
            cs[io++] = '\n'
        }
        return String(cs)
    }

    fun toLeHex(n: Int, byteCount: Int): String {
        var n = n
        val rs = CharArray(byteCount * 2)
        var io = 0
        for (i in 0 until byteCount) {
            rs[io++] = CS[n shr 4 and 15]
            rs[io++] = CS[n shr 0 and 15]
            n = n ushr 8
        }
        return String(rs)
    }

    fun toBeHex(n: Int, byteCount: Int): String {
        var n = n
        val rs = CharArray(byteCount * 2)
        var io = 0
        n = n shl 32 - byteCount * 8
        for (i in 0 until byteCount) {
            rs[io++] = CS[n shr 28 and 15]
            rs[io++] = CS[n shr 24 and 15]
            n = n shl 8
        }
        return String(rs)
    }

    fun convertStringToHex(str: String): String {
        val chars = str.toCharArray()
        val hex = StringBuffer()
        for (i in chars.indices) {
            hex.append(Integer.toHexString(chars[i].toInt()))
        }
        return hex.toString()
    }

    fun convertHexToString(hex: String): String {
        val sb = StringBuilder()
        val temp = StringBuilder()
        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        var i = 0
        while (i < hex.length - 1) {

            //grab the hex in pairs
            val output = hex.substring(i, i + 2)
            //convert hex to decimal
            val decimal = output.toInt(16)
            //convert the decimal to character
            sb.append(decimal.toChar())
            temp.append(decimal)
            i += 2
        }
        return sb.toString()
    }

    fun hexStringToByteArray(data: String?): ByteArray? {
        var data = data
        return if (data == null) {
            null
        } else {
            val result = ByteArray((data.length + 1) / 2)
            if (data.length and 1 == 1) {
                data = data + "0"
            }
            for (i in result.indices) {
                result[i] =
                    ((hex2byte(data[i * 2 + 1]) or hex2byte(data[i * 2])).toInt() shl 4) as Byte
            }
            result
        }
    }

}