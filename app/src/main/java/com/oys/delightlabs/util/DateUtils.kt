package com.oys.delightlabs.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun String.toDate(): Date? {
    val dateString = this
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.parse(dateString)
}

fun Long.toFormattedDateTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return format.format(date)
}

fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.toFormattedDateType2(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM dd", Locale.getDefault())
    return format.format(date)
}

fun Long.toDay(): Int {
    val date = this
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date

    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Long.toDayAfterDays(day: Int): Int {
    val date = this
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date

    calendar.add(Calendar.DAY_OF_MONTH, day)
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Long.toFormattedDateAfter30Days(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.DAY_OF_MONTH, 30)
    val dateAfter30Days = calendar.time
    val format = SimpleDateFormat("MMM dd", Locale.getDefault())
    return format.format(dateAfter30Days)
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

fun Long.toFormattedDateRange(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val startDate = calendar.time
    val startFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val startDateString = startFormat.format(startDate)

    calendar.add(Calendar.DAY_OF_MONTH, 30)
    val endDate = calendar.time
    val endFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val endDateString = endFormat.format(endDate)

    return "$startDateString - $endDateString"
}