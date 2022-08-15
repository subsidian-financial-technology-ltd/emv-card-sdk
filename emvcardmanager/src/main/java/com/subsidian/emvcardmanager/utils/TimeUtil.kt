package com.subsidian.emvcardmanager.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    val TIMEZONE_LAGOS = "Africa/Lagos"

    fun hasExpired(expDate: String): Boolean {
        val expYr = getYearFromExpDate(expDate)
        val expMon = getMonthFromExpDate(expDate)
        val expYrInt = expYr.toInt()
        val expMonInt = expMon.toInt()
        val currYrInt = Calendar.getInstance()[Calendar.YEAR]
        val currMonInt = Calendar.getInstance()[Calendar.MONTH] + 1
        if (expYrInt > currYrInt) return false
        if (expYrInt == currYrInt) {
            if (expMonInt > currMonInt) return false
            if (expMonInt == currMonInt) {
                val dayOfMonth = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                val lastDayOfCurrMonth =
                    Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                if (dayOfMonth < lastDayOfCurrMonth) return false
            }
        }
        return true
    }

    fun getTimeInEpoch(date: Date?): Long {
        try {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(TIMEZONE_LAGOS))
            sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
            val nowStr = sdfDate.format(date)
            val newDate = sdfDate.parse(nowStr)
            if (newDate != null) {
                return newDate.time / 1000
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return System.currentTimeMillis()
    }

    fun getEpoch(milli: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(milli)
        return calendar.time.time
    }

    fun getStartOfDayEpoch(epoch: Long): Long {
        try {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(TIMEZONE_LAGOS))
            sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
            val date = Date(epoch * 1000)
            val cal = Calendar.getInstance()
            cal.time = date
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH] + 1
            val day = cal[Calendar.DAY_OF_MONTH]
            val dateStr = "$year-$month-$day 00:00:00"
            val newDate = sdfDate.parse(dateStr)
            if (newDate != null) {
                return newDate.time / 1000
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return 0
    }

    fun getStartOfDay(date: Date?): Long {
        try {
            val c = Calendar.getInstance()
            c.time = date
            c[Calendar.HOUR_OF_DAY] = 0
            c[Calendar.MINUTE] = 0
            c[Calendar.SECOND] = 0
            c[Calendar.MILLISECOND] = 0
            return c.time.time
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return 0
    }

    fun getEndOfDay(epoch: Long): Long {
        return getStartOfDayEpoch(epoch) + 24 * 60 * 60
    }

    fun getYearFromExpDate(expDate: String): String {
        val expYr = expDate.substring(0, 2)
        val currentYr =
            Calendar.getInstance()[Calendar.YEAR].toString()
        return currentYr.substring(0, 2) + expYr
    }

    fun getMonthFromExpDate(expDate: String): String {
        return expDate.substring(2)
    }

    fun getDateTimeMMddhhmmss(date: Date?): String {
        val sdfDate = SimpleDateFormat("MMddHHmmss", Locale(TIMEZONE_LAGOS))
        sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
        return sdfDate.format(date)
    }

    fun getDateTimeYyyyMMddhhmmss(date: Date?): String {
        val sdfDate = SimpleDateFormat("yyyyMMddHHmmss", Locale(TIMEZONE_LAGOS))
        sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
        return sdfDate.format(date)
    }

    fun getTimehhmmss(date: Date?): String {
        val sdfDate = SimpleDateFormat("HHmmss", Locale(TIMEZONE_LAGOS))
        sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
        return sdfDate.format(date)
    }

    fun getDateMMdd(date: Date?): String {
        val sdfDate = SimpleDateFormat("MMdd", Locale(TIMEZONE_LAGOS))
        sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
        return sdfDate.format(date)
    }

    fun getCustomDate(date: Date?, format: String?): String {
        val sdfDate = SimpleDateFormat(format, Locale.getDefault())
        sdfDate.timeZone = TimeZone.getTimeZone(TIMEZONE_LAGOS)
        return sdfDate.format(date)
    }

    fun dateStringToDate(pattern: String?, dateString: String?): Date {
        return try {
            SimpleDateFormat(pattern).parse(dateString)
        } catch (e: ParseException) {
            Date()
        }
    }
}