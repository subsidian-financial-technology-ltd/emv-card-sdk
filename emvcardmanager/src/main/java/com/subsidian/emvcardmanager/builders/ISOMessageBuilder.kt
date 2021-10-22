package com.subsidian.emvcardmanager.builders

import android.content.Context
import android.util.Log
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.solab.iso8583.parse.ConfigParser
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.enums.MessageType
import com.subsidian.emvcardmanager.enums.ProcCode
import com.subsidian.emvcardmanager.utils.AssetUtils
import com.subsidian.emvcardmanager.utils.StringUtil
import com.subsidian.emvcardmanager.utils.TimeUtil
import com.subsidian.emvcardmanager.security.ValueGenerator
import java.io.StringReader
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.util.*

import com.subsidian.emvcardmanager.exceptions.ISOException


object ISOMessageBuilder {

    var messageFactoryInit = false
    var messageFactory: MessageFactory<IsoMessage>? = null
    var sessionKeyInit = false
    var clearTsk: String? = null

    private var isoMessage: ISOMessage? = null

    fun createMessage(context: Context): ISOMessage {
        isoMessage = ISOMessage(context)
        return isoMessage!!
    }

    class ISOMessage(context: Context) {

        var message: IsoMessage? = null

        init {
            try {
                if (!messageFactoryInit) {
                    val xml = "NIBSS_PACKAGER.xml"
                    val filesArray: Array<String> = AssetUtils.getFilesArrayFromAssets(
                        context, "PACKAGER")
                    for (file in filesArray) {
                        if (file.contains(xml)) {
                            val data: ByteArray = AssetUtils.getFromAssets(context, file)
                            val string = String(data)
                            val stringReader = StringReader(string)
                            messageFactory = ConfigParser.createFromReader(stringReader)
                            messageFactory!!.isUseBinaryBitmap = false
                            messageFactory!!.characterEncoding = StandardCharsets.UTF_8.name()
                            messageFactoryInit = true
                        }
                    }
                }
                if (!sessionKeyInit) {
                    val tmk: String = "TMK"
                    val tsk: String = "TSK"
                    if (!StringUtil.isEmpty(tmk) && !StringUtil.isEmpty(tsk)) {
//                        val clearTmk: String = decryptBase64StringWithRSA(tmk)
//                        clearTsk = decryptWithDES(clearTmk, tsk)
                        sessionKeyInit = true
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @Throws(Exception::class)
        fun createTMKDownloadRequest(isoData: ISOData): ISOMessage {
            val type: Int = MessageType._0800.value.toInt(16)
            val message: IsoMessage = messageFactory!!.newMessage(type)
            val templ: IsoMessage = messageFactory!!.getMessageTemplate(type)
            /** Set ProcCode **/
            message.setValue(
                3,
                isoData.processingCode,
                templ.getField<Any>(3).type,
                templ.getField<Any>(3).length
            )
            /** Set Transaction Time and Date **/
            message.setValue(
                7,
                isoData.transmissionDateTime,
                templ.getField<Any>(7).type,
                templ.getField<Any>(7).length
            )
            /** Set STAN **/
            message.setValue(
                11,
                isoData.systemTraceAuditNumber,
                templ.getField<Any>(11).type,
                templ.getField<Any>(11).length
            )
            /** Set Local Time **/
            message.setValue(
                12,
                isoData.localTime,
                templ.getField<Any>(12).type,
                templ.getField<Any>(12).length
            )
            /** Set Local Date **/
            message.setValue(
                13,
                isoData.localDate,
                templ.getField<Any>(13).type,
                templ.getField<Any>(13).length
            )
            /** Set Terminal ID **/
            message.setValue(
                41,
                isoData.cardAcceptorTerminalId,
                templ.getField<Any>(41).type,
                templ.getField<Any>(41).length
            )
            dumpIsoMessages(message, true)
            this.message = message
            return this
        }

    }


    fun unpackMessage(context: Context, data: ByteArray): UnpackBuilder {
        return UnpackBuilder(context, data)
    }

    class UnpackBuilder(context: Context, private val data: ByteArray?) {

        var message: IsoMessage? = null

        init {
            try {
                if (!messageFactoryInit) {
                    val xml = "NIBSS_PACKAGER.xml"
                    val filesArray: Array<String> = AssetUtils.getFilesArrayFromAssets(
                        context, "PACKAGER")
                    for (file in filesArray) {
                        if (file.contains(xml)) {
                            val data: ByteArray = AssetUtils.getFromAssets(context, file)
                            val string = String(data)
                            val stringReader = StringReader(string)
                            messageFactory = ConfigParser.createFromReader(stringReader)
                            messageFactory!!.isUseBinaryBitmap = false
                            messageFactory!!.characterEncoding = StandardCharsets.UTF_8.name()
                            messageFactoryInit = true
                        }
                    }
                }
                if (!sessionKeyInit) {
                    val tmk: String = "TMK"
                    val tsk: String = "TSK"
                    if (!StringUtil.isEmpty(tmk) && !StringUtil.isEmpty(tsk)) {
//                        val clearTmk: String = decryptBase64StringWithRSA(tmk)
//                        clearTsk = decryptWithDES(clearTmk, tsk)
                        sessionKeyInit = true
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @Throws(ISOException::class)
        fun build(): UnpackBuilder {
            try {
                val isoMessage: IsoMessage = messageFactory!!.parseMessage(data, 0)
                dumpIsoMessages(isoMessage, false)
                this.message = isoMessage
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return this
        }
    }

    private fun dumpIsoMessages(message: IsoMessage, isRequest: Boolean) {
        val isoString = StringBuilder()
        isoString.append("********************************************\n")
        isoString.append("============================================\n")
        isoString.append("      ISO DATA DUMP(${if (isRequest){"Request"} else {"Response"}})\n")
        isoString.append("============================================\n")
        try {
            for (i in 0..128) {
                if (message.hasField(i)) {
                    isoString.append(
                        "Field $i (${isoFieldName(i)})  => ${message.getField<Any>(i)} \n"
                    )
                }
            }
            isoString.append("============================================\n")
            Log.d(this.javaClass.simpleName, isoString.toString())
            //System.out.println(isoString.toString());
        } catch (ex: Exception) {
            isoString.append("============================================\n")
            Log.d(this.javaClass.simpleName, isoString.toString())
            ex.printStackTrace()
        }
    }

    private fun isoFieldName(field: Int): String {
        return when(field) {
            2 -> "Primary account number - Mandatory"
            3 -> "Processing code - Mandatory"
            4 -> "Amount, transaction - Mandatory"
            7 -> "Transmission date and time - Conditional"
            9 -> "Conversion rate, settlement - Conditional"
            11 -> "Systems trace audit number - Conditional"
            12 -> "Time, local transaction - Mandatory"
            13 -> "Date, local transaction - Conditional"
            14 -> "Date, expiration - Conditional"
            15 -> "Date, Settlement - Conditional"
            16 -> "Date, conversion - Conditional"
            18 -> "Merchant’s type - Mandatory"
            22 -> "POS entry mode - Mandatory"
            23 -> "Card sequence number - Conditional"
            25 -> "POS condition code - Mandatory"
            26 -> "POS PIN capture code - Conditional"
            28 -> "Amount, transaction fee - Conditional"
            29 -> "Amount, settlement fee - Conditional"
            30 -> "Amount, transaction processing fee - Conditional"
            31 -> "Amount, settle processing fee - Conditional"
            32 -> "Acquiring institution id code - Mandatory"
            33 -> "Forwarding institution id code - Conditional"
            35 -> "Track 2 data - Conditional"
            37 -> "Retrieval reference number - Mandatory"
            38 -> "Authorization id response - Conditional"
            39 -> "Response code - Mandatory"
            40 -> "Service restriction code - Conditional"
            41 -> "Card acceptor terminal id - Optional"
            42 -> "Card acceptor id code - Conditional"
            43 -> "Card acceptor name/location - Conditional"
            44 -> "Additional response data - Optional"
            48 -> "Additional data - Conditional"
            49 -> "Currency code, transaction - Mandatory"
            50 -> "Currency code, settlement - Conditional"
            52 -> "PIN data - Conditional"
            53 -> "Security related control information - Conditional"
            54 -> "Additional amounts - Conditional"
            55 -> "Integrated Circuit Card System Related Data - Conditional"
            56 -> "Message reason code - Optional"
            58 -> "Authorizing agent id code - Conditional"
            59 -> "Transport (echo) data - Conditional"
            60 -> "Payment Information - Conditional"
            62 -> "Private, management data 1 - Conditional"
            63 -> "Private, management data 2 - Conditional"
            64 -> "Primary Message Hash Value - Conditional"
            67 -> "Extended payment code - Conditional"
            90 -> "Original data elements - Mandatory"
            95 -> "Replacement Amounts - Mandatory"
            98 -> "Payee - Conditional"
            100 -> "Receiving Institution ID Code - Optional"
            102 -> "Account identification 1 - Optional"
            103 -> "Account identification 2 - Optional"
            123 -> "POS data code - Mandatory"
            124 -> "Near Field Communication Data - Conditional"
            128 -> "Secondary Message Hash Value - Mandatory"
            else -> "Unknown Field"
        }
    }
}