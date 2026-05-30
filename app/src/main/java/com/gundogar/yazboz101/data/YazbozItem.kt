package com.gundogar.yazboz101.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "yazboz_item")
data class YazbozItem(
    val players: List<Player>,
    val gameMode: GameMode = GameMode.INDIVIDUAL,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    )