package com.example.assignment11bapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignment11bapplication.database.DataBaseNews


@Dao
interface NewsDao {
    @Query("SELECT * FROM DataBaseNews")
    suspend fun getNewsList(): List<DataBaseNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dets: List<DataBaseNews>)

    @Query("DELETE FROM DataBaseNews")
    suspend fun clearAll()

}