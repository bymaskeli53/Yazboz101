package com.gundogar.yazboz101.util

import androidx.room.TypeConverter
import com.gundogar.yazboz101.data.GameMode

class GameModeConverter {

    @TypeConverter
    fun fromGameMode(value: GameMode): String = value.name

    @TypeConverter
    fun toGameMode(value: String): GameMode =
        runCatching { GameMode.valueOf(value) }.getOrDefault(GameMode.INDIVIDUAL)
}
