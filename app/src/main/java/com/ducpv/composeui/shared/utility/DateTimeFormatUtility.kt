package com.ducpv.composeui.shared.utility

import android.content.Context
import com.ducpv.composeui.R
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

enum class ModifiedUnit {
    DAY_OF_MONTH, HOUR_OF_DAY, MINUTE
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

fun Date.toFormattedDurationTime(context: Context): String {
    val fromDate = this
    val toDate = Date()
    val durationMills = (toDate.time - fromDate.time)
    return when {
        durationMills < 1_000L -> { // < 1 second -> "Now"
            context.getString(R.string.now)
        }
        durationMills in 1_000L..60_000L -> { // < 1 minutes -> "xxx seconds"
            context.getString(R.string.seconds_format, durationMills / 1_000L)
        }
        durationMills in 60_000L..60 * 60_000L -> { // < 1 hours -> "xxx minutes"
            context.getString(R.string.minutes_format, durationMills / 60_000L)
        }
        durationMills in 60 * 60_000L..24 * 60 * 60_000L -> { // < 1 days -> "xxx hours"
            context.getString(R.string.hours_format, durationMills / (60 * 60_000L))
        }
        durationMills in 24 * 60 * 60_000L..7 * 24 * 60 * 60_000L -> { // < 7 days -> "xxx days"
            context.getString(R.string.days_format, durationMills / (24 * 60 * 60_000L))
        }
        else -> {
            val fromCalendar = Calendar.getInstance().apply { time = fromDate }
            val toCalendar = Calendar.getInstance().apply { time = toDate }
            when {
                fromCalendar.get(Calendar.YEAR) != toCalendar.get(Calendar.YEAR) -> { // -
                    this.toFormattedString("dd/MM/YYYY")
                }
                else -> {
                    this.toFormattedString("dd/MM")
                }
            }
        }
    }
}
