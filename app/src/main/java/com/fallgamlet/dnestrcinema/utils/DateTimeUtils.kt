package com.fallgamlet.dnestrcinema.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@SuppressLint("ConstantLocale")
object DateTimeUtils {

    private val dateFormatWithoutTimezone: SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale) }
    private val dateFormatISO: SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", locale) }
    private val dateFormatDotWithoutTime: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy", locale) }
    private val dateFormatTimeOnly: SimpleDateFormat by lazy { SimpleDateFormat("HH:mm", locale) }
    private val dateFormatDot: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy HH:mm:ss", locale) }
    private val dateFormatNamed: SimpleDateFormat by lazy { SimpleDateFormat("d MMMM yyyy", locale) }
    private val dateFormatDayOnlyFullNamed: SimpleDateFormat by lazy { SimpleDateFormat("E, dd MMMM", locale) }
    private val locale = Locale.getDefault()

    private val monthNamesByRussian: Array<String> by lazy { arrayOf("январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь") }
    private val monthNamesByEnglish: Array<String> by lazy { arrayOf("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december") }

    val SECOND: Long = 1000
    val MINUTE = 60 * SECOND
    val HOUR = 60 * MINUTE
    val DAY = 24 * HOUR

    fun getISO8601Date(date: Date?): String? {
        date ?: return null
        val dateFormat = dateFormatISO
        val s = dateFormat.format(date)
        return if (dateFormat.toPattern().contains("Z")) {
            StringBuilder(s).insert(s.length - 2, ':').toString()
        } else {
            s
        }
    }

    fun getISODate(date: Date?): String? {
        date ?: return null
        return dateFormatISO.format(date)
    }

    fun iso8601ToDate(datetimeStr: String?): Date? {
        datetimeStr ?: return null
        var date: Date? = null
        try {
            date = dateFormatISO.parse(datetimeStr)
        } catch (ignore: Exception) {
        }

        return date
    }

    fun getISODateWithoutTimezone(date: Date?): String? {
        date ?: return null
        return dateFormatWithoutTimezone.format(date)
    }

    fun isoWithoutTimezoneToDate(datetimeStr: String?): Date? {
        datetimeStr ?: return null
        return try {
            dateFormatWithoutTimezone.parse(datetimeStr)
        } catch (ignore: Exception) {
            null
        }
    }

    fun getDateDotWithoutTime(date: Date?): String? {
        date ?: return null
        return dateFormatDotWithoutTime.format(date)
    }

    fun getTimeOnly(date: Date?): String? {
        return if (date == null) {
            null
        } else dateFormatTimeOnly.format(date)
    }

    fun getDateDot(date: Date?): String? {
        date ?: return null
        return dateFormatDot.format(date)
    }

    fun getDateNamed(date: Date?): String? {
        date ?: return null
        return dateFormatNamed.format(date)
    }

    fun getDateDayOnlyFullNamed(date: Date?): String? {
        date ?: return null
        return dateFormatDayOnlyFullNamed.format(date)
    }

    fun getDateWithoutTime(calendar: Calendar?, date: Date?): Date? {
        calendar ?: return null
        date ?: return null
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getDateWithoutTime(date: Date?): Date? {
        date ?: return null
        val calendar = Calendar.getInstance()
        return getDateWithoutTime(calendar, date)
    }

    fun addDays(date: Date?, days: Int): Date? {
        date ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    fun addHours(date: Date?, hours: Int): Date? {
        date ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }

    fun addMinutes(date: Date?, minutes: Int): Date? {
        date ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }

    fun addMilliseconds(date: Date?, milliseconds: Int): Date? {
        date ?: return null
        val time = date.time + milliseconds
        return Date(time)
    }

    fun getDuration(start: Date, end: Date): Long {
        return end.time - start.time
    }



    @JvmOverloads
    fun getMonthName(month: Int, locale: Locale? = Locale.getDefault()): String {
        locale ?: return ""

        val months = when (locale.language) {
            "ru" -> monthNamesByRussian
            else -> monthNamesByEnglish
        }

        return when (0 <= month && month < months.size) {
            true -> months[month]
            else -> ""
        }
    }

    fun getNamedMonthYear(date: Date, locale: Locale): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getNamedMonthYear(calendar, locale)
    }

    fun getNamedMonthYear(date: Calendar?, locale: Locale): String {
        date ?: return ""
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        val monthName = getMonthName(month, locale)
        return "$monthName $year"
    }

}
