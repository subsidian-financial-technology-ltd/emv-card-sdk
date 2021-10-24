package com.subsidian.emvcardmanager.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

object AssetUtil {
    fun getFilesArrayFromAssets(context: Context, path: String): Array<String>? {
        val resources = context.resources
        val assetManager = resources.assets
        var files: Array<String>? = null
        try {
            files = assetManager.list(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (files != null) {
            for (i in files.indices) {
                files[i] = path + "/" + files[i]
            }
        }
        return files
    }

    fun getFromAssets(path: String?): ByteArray? {
        var `in`: InputStream? = null
        try {
            `in` = AssetUtil::class.java.classLoader.getResource("PACKAGER/NIBSS_PACKAGER.xml")
                .openStream()
            //获取文件的字节数
            val lenght = `in`.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            //将文件中的数据读到byte数组中
            `in`.read(buffer)
            return buffer
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getFromAssets(context: Context, fileName: String?): ByteArray? {
        var `in`: InputStream? = null
        try {
            `in` = context.resources.assets.open(fileName!!)
            val lenght = `in`.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            `in`.read(buffer)
            return buffer
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getInputStreamFromAssets(context: Context, fileName: String?): InputStream? {
        var `in`: InputStream? = null
        try {
            `in` = context.resources.assets.open(fileName!!)
            val lenght = `in`.available()
            return `in`
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
}