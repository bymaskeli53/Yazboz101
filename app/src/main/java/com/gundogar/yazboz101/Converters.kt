package com.gundogar.yazboz101

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

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
