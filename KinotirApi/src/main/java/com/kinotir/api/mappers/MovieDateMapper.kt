package com.kinotir.api.mappers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal class MovieDateMapper : Mapper<String?, Date?> {

    override fun map(src: String?): Date {
        return try {
            if (src.isNullOrBlank()) return Date(0)

            val format = "'с' d MMMM, yyyy" // с 16 марта, 2017
            val locale = Locale("ru", "RU")
            SimpleDateFormat(format, locale).parse(src.trim { it <= ' ' })
        } catch (e: ParseException) {
            Date(0)
        }
    }
}
