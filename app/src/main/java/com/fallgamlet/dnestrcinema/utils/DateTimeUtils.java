package com.fallgamlet.dnestrcinema.utils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fallgamlet on 05.07.16.
 */
public class DateTimeUtils {
    //region Static fields
    private static SimpleDateFormat mDateFormatWithoutTimezone;
    private static SimpleDateFormat mDateFormatISO;
    private static SimpleDateFormat mDateFormatDotWithoutTime;
    private static SimpleDateFormat mDateFormatTimeOnly;
    private static SimpleDateFormat mDateFormatDot;
    private static SimpleDateFormat mDateFormatNamed;
    private static SimpleDateFormat mDateFormatDayOnlyFullNamed;

    public static final long SECOND = 1000;
    public static final long MINUTE = 60*SECOND;
    public static final long HOUR = 60*MINUTE;
    public static final long DAY = 24*HOUR;
    //endregion

    //region Methods
    private static SimpleDateFormat getDateFormatISO() {
        if (mDateFormatISO == null) { mDateFormatISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()); }
        return mDateFormatISO;
    }
    public static String getISO8601Date(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatISO();
        String s =  dateFormat.format(date);
        if (dateFormat.toPattern().contains("Z")) {
            return new StringBuilder(s).insert(s.length()-2, ':').toString();
        } else {
            return s;
        }
    }
    public static String getISODate(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatISO();
        return dateFormat.format(date);
    }
    public static Date iso8601ToDate(String datetimeStr) {
        if (datetimeStr == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatISO();
        Date date = null;
        try {
            date = dateFormat.parse(datetimeStr);
        } catch (Exception ignore) {}
        return date;
    }

    private static SimpleDateFormat getDateFormatWithoutTimezone() {
        if (mDateFormatWithoutTimezone == null) { mDateFormatWithoutTimezone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()); }
        return mDateFormatWithoutTimezone;
    }
    public static String getISODateWithoutTimezone(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatWithoutTimezone();
        return dateFormat.format(date);
    }
    public static Date isoWithoutTimezoneToDate(String datetimeStr) {
        if (datetimeStr == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatWithoutTimezone();
        Date date = null;
        try {
            date = dateFormat.parse(datetimeStr);
        } catch (Exception ignore) {}
        return date;
    }

    private static SimpleDateFormat getDateFormatDotWithoutTime() {
        if (mDateFormatDotWithoutTime == null) { mDateFormatDotWithoutTime = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()); }
        return mDateFormatDotWithoutTime;
    }
    public static String getDateDotWithoutTime(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatDotWithoutTime();
        return dateFormat.format(date);
    }

    private static SimpleDateFormat getDateFormatTimeOnly() {
        if (mDateFormatTimeOnly == null) { mDateFormatTimeOnly = new SimpleDateFormat("HH:mm", Locale.getDefault()); }
        return mDateFormatTimeOnly;
    }
    public static String getTimeOnly(Date date) {
        if (date == null) { return null; }
        return getDateFormatTimeOnly().format(date);
    }

    private static SimpleDateFormat getDateFormatDot() {
        if (mDateFormatDot == null) { mDateFormatDot = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()); }
        return mDateFormatDot;
    }
    public static String getDateDot(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatDot();
        return dateFormat.format(date);
    }

    private static SimpleDateFormat getDateFormatNamed() {
        if (mDateFormatNamed == null) { mDateFormatNamed = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()); }
        return mDateFormatNamed;
    }
    public static String getDateNamed(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatNamed();
        return dateFormat.format(date);
    }

    private static SimpleDateFormat getDateFormatDayOnlyFullNamed() {
        if (mDateFormatDayOnlyFullNamed == null) { mDateFormatDayOnlyFullNamed = new SimpleDateFormat("E, dd MMMM", Locale.getDefault()); }
        return mDateFormatDayOnlyFullNamed;
    }
    public static String getDateDayOnlyFullNamed(Date date) {
        if (date == null) { return null; }
        SimpleDateFormat dateFormat = getDateFormatDayOnlyFullNamed();
        return dateFormat.format(date);
    }

    public static Date getDateWithoutTime(Calendar calendar, Date date) {
        if (calendar == null || date == null) { return null; }
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date getDateWithoutTime(Date date) {
        if (date == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        return getDateWithoutTime(calendar, date);
    }

    public static Date addDays(Date date, int days) {
        if (date == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
    public static Date addHours(Date date, int hours) {
        if (date == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
    public static Date addMinutes(Date date, int minutes) {
        if (date == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
    public static Date addMilliseconds(Date date, int milliseconds) {
        if (date == null) { return null; }
        long time = date.getTime() + milliseconds;
        return new Date(time);
    }

    public static long getDuration(@NonNull Date start, @NonNull Date end) {
        return end.getTime() - start.getTime();

    }

    //region Month names singletons
    private static String[] monthNamesByRussian = null;
    public static String[] getMonthNamesByRussian() {
        if (monthNamesByRussian == null) {
            monthNamesByRussian = new String[] {"январь","февраль","март","апрель","май","июнь","июль","август","сентябрь","октябрь","ноябрь","декабрь"};
        }
        return monthNamesByRussian;
    }

    private static String[] monthNamesByEnglish = null;
    public static String[] getMonthNamesByEnglish() {
        if (monthNamesByEnglish == null) {
            monthNamesByEnglish = new String[] {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        }
        return monthNamesByEnglish;
    }
    //endregion

    public static String getMonthName(int month) {
        return getMonthName(month, Locale.getDefault());
    }
    public static String getMonthName(int month, Locale locale) {
        if (locale == null) { return null; }
        String name = null;
        String[] months = null;
        String lang = locale.getLanguage();
        if (lang.equals("ru")) { months = getMonthNamesByRussian(); }
        else { months = getMonthNamesByEnglish(); }

        if (0<=month && month<months.length) { name = months[month]; }
        return name;
    }

    public static String getNamedMonthYear(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getNamedMonthYear(calendar, locale);
    }
    public static String getNamedMonthYear(Calendar date, Locale locale) {
        if (date == null) { return null; }
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String monthName = getMonthName(month, locale);
        return monthName+" "+year;
    }
    //endregion
}
