package com.acdfirstproject

import java.util.*
import java.util.concurrent.TimeUnit

fun Long.convertMillisecondsToHours(): String {
    var milliSeconds = this
    val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
    milliSeconds -= TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
    milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)

    return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
}