package com.gundogar.yazboz101

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yazboz_item")
data class YazbozItem(
    val players: List<Player>,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)