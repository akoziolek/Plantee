package com.example.plantee.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun LocalDate.toMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun convertMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertLocalDateToDateString(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.getDefault())
    return date.format(formatter)
}