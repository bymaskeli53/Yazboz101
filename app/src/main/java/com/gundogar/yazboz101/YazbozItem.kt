package com.gundogar.yazboz101

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "YazbozItem")
data class YazbozItem(val name: String,
                      @PrimaryKey(autoGenerate = true)
                      val id: Int,
                      val scores: List<Int>,)
