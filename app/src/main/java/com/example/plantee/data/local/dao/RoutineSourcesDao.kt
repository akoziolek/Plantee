package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.RoutineSourceEntity

@Dao
interface RoutineSourcesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(routineSource: RoutineSourceEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(routineSource: List<RoutineSourceEntity>): List<Long>

    @Update
    suspend fun update(routineSource: RoutineSourceEntity)

    @Delete
    suspend fun delete(routineSource: RoutineSourceEntity)
}