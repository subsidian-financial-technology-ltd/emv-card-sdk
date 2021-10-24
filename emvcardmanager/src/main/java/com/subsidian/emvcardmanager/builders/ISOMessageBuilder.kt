package com.subsidian.emvcardmanager.builders

import android.content.Context
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.solab.iso8583.parse.ConfigParser
import com.subsidian.emvcardmanager.builders.transactions.*
import com.subsidian.emvcardmanager.entities.ISOData
import com.subsidian.emvcardmanager.entities.ISOMessage
import com.subsidian.emvcardmanager.exceptions.ISOException
import com.subsidian.emvcardmanager.utils.*
import java.io.StringReader
import java.nio.charset.StandardCharsets


object ISOMessageBuilder {

    var messageFactoryInit = false
    var messageFactory: MessageFactory<IsoMessage>? = null
    var sessionKeyInit = false
    var clearTsk: String? = null

    private var packBuilder: PackBuilder? = null

    fun packMessage(context: Context, terminalMasterKey: String = "", terminalSessionKey: String = ""): PackBuilder {
        packBuilder = PackBuilder(context,terminalMasterKey, terminalSessionKey)
        return packBuilder!!
    }

    class PackBuilder(context: Context, private val terminalMasterKey: String = "", private val terminalSessionKey: String = "") {

        var message: ISOMessage? = null

        init {
            try {
                if (!messageFactoryInit) {
                    val xml = "NIBSS_PACKAGER.xml"
                    val filesArray: Array<String>? = AssetUtil.getFilesArrayFromAssets(
                        context, "PACKAGER")
                    for (file in filesArray!!) {
                        if (file.contains(xml)) {
                            val data: ByteArray? = AssetUtil.getFromAssets(context, file)
                            val string = String(data!!)
                            val stringReader = StringReader(string)
                            messageFactory = ConfigParser.createFromReader(stringReader)
                            messageFactory!!.isUseBinaryBitmap = false
                            messageFactory!!.characterEncoding = StandardCharsets.UTF_8.name()
                            messageFactoryInit = true
                        }
                    }
                }
                if (!sessionKeyInit && (!StringUtil.isEmpty(terminalMasterKey) && !StringUtil.isEmpty(terminalSessionKey))) {
//                        val clearTmk: String = decryptBase64StringWithRSA(tmk)
                    clearTsk = KeyUtil().decryptWithDES(terminalMasterKey, terminalSessionKey)
                    sessionKeyInit = true
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @Throws(Exception::class)
        fun createTMKDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(TMKRequestBuilder.build(isoData, messageFactory!!))
        }

        @Throws(Exception::class)
        fun createTPKDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(TPKRequestBuilder.build(isoData, messageFactory!!))
        }

        @Throws(Exception::class)
        fun createTSKDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(TSKRequestBuilder.build(isoData, messageFactory!!))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createAIDDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(AIDRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createCAPKDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(CAPKRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createIPEKTrackTwoRequest(isoData: ISOData): PackBuilder {
            return echoMessage(IPEKTrackTwoRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createIPEKEMVRequest(isoData: ISOData): PackBuilder {
            return echoMessage(IPEKEMVRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createTerminalParameterDownloadRequest(isoData: ISOData): PackBuilder {
            return echoMessage(TerminalParameterRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @kotlin.jvm.Throws(Exception::class)
        fun createCallHomeRequest(isoData: ISOData): PackBuilder {
            return echoMessage(CallHomeRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @Throws(Exception::class)
        fun createPurchaseRequest(isoData: ISOData): PackBuilder {
            return echoMessage(PurchaseRequestBuilder.build(isoData, messageFactory!!, terminalSessionKey))
        }

        @Throws(Exception::class)
        private fun echoMessage(message: ISOMessage) : PackBuilder {
            PrintUtil.dumpIsoMessages(message, true)
            this.message = message
            return this
        }

    }


    fun unpackMessage(context: Context, data: ByteArray): UnpackBuilder {
        return UnpackBuilder(context, data)
    }

    class UnpackBuilder(context: Context, private val data: ByteArray?) {

        init {
            try {
                if (!messageFactoryInit) {
                    val xml = "NIBSS_PACKAGER.xml"
                    val filesArray: Array<String>? = AssetUtil.getFilesArrayFromAssets(
                        context, "PACKAGER")
                    for (file in filesArray!!) {
                        if (file.contains(xml)) {
                            val data: ByteArray? = AssetUtil.getFromAssets(context, file)
                            val string = String(data!!)
                            val stringReader = StringReader(string)
                            messageFactory = ConfigParser.createFromReader(stringReader)
                            messageFactory!!.isUseBinaryBitmap = false
                            messageFactory!!.characterEncoding = StandardCharsets.UTF_8.name()
                            messageFactoryInit = true
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @Throws(ISOException::class)
        fun unpack(): ISOData {
            return try {
                val isoMessage = ISOMessage(messageFactory!!.parseMessage(data, 0) as IsoMessage)
                PrintUtil.dumpIsoMessages(isoMessage, false)
                VariableCheckUtil.unpackISOMessage(isoMessage)
            } catch (ex: Exception) {
                ex.printStackTrace()
                ISOData()
            }
        }
    }

}