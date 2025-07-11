package com.gundogar.yazboz101.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gundogar.yazboz101.data.Player

class PlayerListConverter {

    @TypeConverter
    fun fromPlayerList(value: List<Player>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPlayerList(value: String): List<Player> {
        val listType = object : TypeToken<List<Player>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
