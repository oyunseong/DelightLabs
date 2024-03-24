package com.oys.delightlabs.util

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


fun String.toDate(): Date? {
    val dateString = this
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.parse(dateString)
}

fun String.toTime(): String? {
    return try {
        val date = this.toDate()
        if (date != null) {
            val format = SimpleDateFormat("hh.mm a", Locale.getDefault())
            format.format(date)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}