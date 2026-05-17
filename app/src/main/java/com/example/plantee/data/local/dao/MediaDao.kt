package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.plantee.data.local.entities.MediaEntity

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(media: MediaEntity): Long

    @Query("DELETE FROM media WHERE id = :id")
    suspend fun deleteById(id: Long)
}