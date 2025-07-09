package com.gundogar.yazboz101

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface YazbozDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(item: YazbozItem)

    @Query("SELECT * FROM yazboz_item ORDER BY id DESC")
    fun getAllGames(): Flow<List<YazbozItem>>
}