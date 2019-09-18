package com.fallgamlet.dnestrcinema.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by fallgamlet on 05.07.16.
 */
object DateTimeUtils {
    //region Static fields
    private var mDateFormatWithoutTimezone: SimpleDateFormat? = null
    private var mDateFormatISO: SimpleDateFormat? = null
    private var mDateFormatDotWithoutTime: SimpleDateFormat? = null
    private var mDateFormatTimeOnly: SimpleDateFormat? = null
    private var mDateFormatDot: SimpleDateFormat? = null
    private var mDateFormatNamed: SimpleDateFormat? = null
    private var mDateFormatDayOnlyFullNamed: SimpleDateFormat? = null

    val SECOND: Long = 1000
    val MINUTE = 60 * SECOND
    val HOUR = 60 * MINUTE
    val DAY = 24 * HOUR
    //endregion

    //region Methods
    private val dateFormatISO: SimpleDateFormat
        get() {
            if (mDateFormatISO == null) {
                mDateFormatISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
            }
            return mDateFormatISO
        }

    private val dateFormatWithoutTimezone: SimpleDateFormat
        get() {
            if (mDateFormatWithoutTimezone == null) {
                mDateFormatWithoutTimezone =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            }
            return mDateFormatWithoutTimezone
        }

    private val dateFormatDotWithoutTime: SimpleDateFormat
        get() {
            if (mDateFormatDotWithoutTime == null) {
                mDateFormatDotWithoutTime = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            }
            return mDateFormatDotWithoutTime
        }

    private val dateFormatTimeOnly: SimpleDateFormat
        get() {
            if (mDateFormatTimeOnly == null) {
                mDateFormatTimeOnly = SimpleDateFormat("HH:mm", Locale.getDefault())
            }
            return mDateFormatTimeOnly
        }

    private val dateFormatDot: SimpleDateFormat
        get() {
            if (mDateFormatDot == null) {
                mDateFormatDot = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            }
            return mDateFormatDot
        }

    private val dateFormatNamed: SimpleDateFormat
        get() {
            if (mDateFormatNamed == null) {
                mDateFormatNamed = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            }
            return mDateFormatNamed
        }

    private val dateFormatDayOnlyFullNamed: SimpleDateFormat
        get() {
            if (mDateFormatDayOnlyFullNamed == null) {
                mDateFormatDayOnlyFullNamed = SimpleDateFormat("E, dd MMMM", Locale.getDefault())
            }
            return mDateFormatDayOnlyFullNamed
        }

    //region Month names singletons
    private var monthNamesByRussian: Array<String>? = null

    private var monthNamesByEnglish: Array<String>? = null
    fun getISO8601Date(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatISO
        val s = dateFormat.format(date)
        return if (dateFormat.toPattern().contains("Z")) {
            StringBuilder(s).insert(s.length - 2, ':').toString()
        } else {
            s
        }
    }

    fun getISODate(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatISO
        return dateFormat.format(date)
    }

    fun iso8601ToDate(datetimeStr: String?): Date? {
        if (datetimeStr == null) {
            return null
        }
        val dateFormat = dateFormatISO
        var date: Date? = null
        try {
            date = dateFormat.parse(datetimeStr)
        } catch (ignore: Exception) {
        }

        return date
    }

    fun getISODateWithoutTimezone(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatWithoutTimezone
        return dateFormat.format(date)
    }

    fun isoWithoutTimezoneToDate(datetimeStr: String?): Date? {
        if (datetimeStr == null) {
            return null
        }
        val dateFormat = dateFormatWithoutTimezone
        var date: Date? = null
        try {
            date = dateFormat.parse(datetimeStr)
        } catch (ignore: Exception) {
        }

        return date
    }

    fun getDateDotWithoutTime(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatDotWithoutTime
        return dateFormat.format(date)
    }

    fun getTimeOnly(date: Date?): String? {
        return if (date == null) {
            null
        } else dateFormatTimeOnly.format(date)
    }

    fun getDateDot(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatDot
        return dateFormat.format(date)
    }

    fun getDateNamed(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatNamed
        return dateFormat.format(date)
    }

    fun getDateDayOnlyFullNamed(date: Date?): String? {
        if (date == null) {
            return null
        }
        val dateFormat = dateFormatDayOnlyFullNamed
        return dateFormat.format(date)
    }

    fun getDateWithoutTime(calendar: Calendar?, date: Date?): Date? {
        if (calendar == null || date == null) {
            return null
        }
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getDateWithoutTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val calendar = Calendar.getInstance()
        return getDateWithoutTime(calendar, date)
    }

    fun addDays(date: Date?, days: Int): Date? {
        if (date == null) {
            return null
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    fun addHours(date: Date?, hours: Int): Date? {
        if (date == null) {
            return null
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }

    fun addMinutes(date: Date?, minutes: Int): Date? {
        if (date == null) {
            return null
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }

    fun addMilliseconds(date: Date?, milliseconds: Int): Date? {
        if (date == null) {
            return null
        }
        val time = date.time + milliseconds
        return Date(time)
    }

    fun getDuration(start: Date, end: Date): Long {
        return end.time - start.time

    }

    fun getMonthNamesByRussian(): Array<String> {
        if (monthNamesByRussian == null) {
            monthNamesByRussian = arrayOf(
                "январь",
                "февраль",
                "март",
                "апрель",
                "май",
                "июнь",
                "июль",
                "август",
                "сентябрь",
                "октябрь",
                "ноябрь",
                "декабрь"
            )
        }
        return monthNamesByRussian
    }

    fun getMonthNamesByEnglish(): Array<String> {
        if (monthNamesByEnglish == null) {
            monthNamesByEnglish = arrayOf(
                "january",
                "february",
                "march",
                "april",
                "may",
                "june",
                "july",
                "august",
                "september",
                "october",
                "november",
                "december"
            )
        }
        return monthNamesByEnglish
    }

    @JvmOverloads
    fun getMonthName(month: Int, locale: Locale? = Locale.getDefault()): String? {
        if (locale == null) {
            return null
        }
        var name: String? = null
        var months: Array<String>? = null
        val lang = locale.language
        if (lang == "ru") {
            months = getMonthNamesByRussian()
        } else {
            months = getMonthNamesByEnglish()
        }

        if (0 <= month && month < months.size) {
            name = months[month]
        }
        return name
    }

    fun getNamedMonthYear(date: Date, locale: Locale): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getNamedMonthYear(calendar, locale)
    }

    fun getNamedMonthYear(date: Calendar?, locale: Locale): String? {
        if (date == null) {
            return null
        }
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        val monthName = getMonthName(month, locale)
        return "$monthName $year"
    }
    //endregion
}//endregion
