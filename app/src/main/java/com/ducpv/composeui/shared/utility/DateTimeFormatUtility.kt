package com.ducpv.composeui.shared.utility

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pvduc9773 on 12/05/2023.
 */
class DateTimeFormatException(ex: Exception? = null) : Exception(ex?.message ?: "Failed to format date time")

fun String.toDate(pattern: String): Date = try {
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this) ?: throw DateTimeFormatException()
} catch (e: Exception) {
    throw DateTimeFormatException(e)
}

fun String.toDateUTC(pattern: String): Date = try {
    SimpleDateFormat(pattern, Locale.getDefault()).apply {
        this.timeZone = TimeZone.getTimeZone("GMT")
    }.parse(this) ?: throw DateTimeFormatException()
} catch (e: Exception) {
    throw DateTimeFormatException(e)
}

fun Date.toFormattedString(pattern: String): String = try {
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)
} catch (e: Exception) {
    throw DateTimeFormatException(e)
}

fun Date.modified(value: Int, unit: ModifiedUnit): Date {
    return when (unit) {
        ModifiedUnit.DAY_OF_MONTH -> {
            Calendar.getInstance().apply {
                time = this@modified
                add(Calendar.DAY_OF_MONTH, value)
            }.time
        }
        ModifiedUnit.HOUR_OF_DAY -> {
            Calendar.getInstance().apply {
                time = this@modified
                add(Calendar.HOUR_OF_DAY, value)
            }.time
        }
        ModifiedUnit.MINUTE -> {
            Calendar.getInstance().apply {
                time = this@modified
                add(Calendar.MINUTE, value)
            }.time
        }
    }
}

enum class ModifiedUnit {
    DAY_OF_MONTH, HOUR_OF_DAY, MINUTE
}
