package com.ducpv.composeui.shared.utility

import java.util.concurrent.TimeUnit

/**
 * Created by pvduc9773 on 12/05/2023.
 */
fun Long.millisecondToTimeFormat(): String { // "00:00:00"
    var milliseconds = this
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    milliseconds -= TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
}
