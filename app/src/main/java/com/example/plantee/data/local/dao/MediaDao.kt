package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.Media

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(media: Media)

    @Update
    suspend fun update(media: Media)

    @Delete
    suspend fun delete(media: Media)
}