package com.gundogar.yazboz101.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Entity(tableName = "yazboz_item")
data class YazbozItem(
    val players: List<Player>,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    )

fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("tr"))
    return dateTime.format(formatter)
}