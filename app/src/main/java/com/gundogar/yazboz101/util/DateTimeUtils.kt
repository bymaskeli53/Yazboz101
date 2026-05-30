package com.gundogar.yazboz101.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("tr"))
    return dateTime.format(formatter)
}
