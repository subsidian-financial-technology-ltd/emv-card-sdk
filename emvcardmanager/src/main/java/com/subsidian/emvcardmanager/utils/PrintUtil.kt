package com.subsidian.emvcardmanager.utils

import android.util.Log
import com.subsidian.emvcardmanager.entities.ISOMessage

object PrintUtil {

    fun dumpIsoMessages(message: ISOMessage, isRequest: Boolean) {
        val isoString = StringBuilder()
        isoString.append("-\n")
        isoString.append("============================================\n")
        isoString.append("ISO DATA DUMP(${VariableCheckUtil.isoMessageType(message)} - ${if (isRequest){"Request"} else {"Response"}})\n")
        isoString.append("============================================\n")
        isoString.append("ISO TYPE: ${message.getMessageType()}\n")
        isoString.append("============================================\n")
        isoString.append("Debug Message: ${message.getMessage().debugString()}\n")
        isoString.append("============================================\n")
        try {
            for (i in 0..128) {
                if (message.getMessage().hasField(i)) {
                    isoString.append(
                        "Field $i (${VariableCheckUtil.isoFieldName(i)})  => ${message.getMessage().getField<Any>(i)} \n"
                    )
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            isoString.append("============================================\n")
            Log.d(this.javaClass.simpleName, isoString.toString())
        }
    }

}