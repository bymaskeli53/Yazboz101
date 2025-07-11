package com.gundogar.yazboz101.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gundogar.yazboz101.util.LocalDateTimeConverter
import com.gundogar.yazboz101.util.PlayerListConverter

@Database(entities = [YazbozItem::class], version = 1)
@TypeConverters(PlayerListConverter::class, LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun yazbozDao(): YazbozDao
}