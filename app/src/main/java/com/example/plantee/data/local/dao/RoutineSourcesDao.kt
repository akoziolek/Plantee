package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.RoutineSource

@Dao
interface RoutineSourcesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routineSource: RoutineSource)

    @Update
    suspend fun update(routineSource: RoutineSource)

    @Delete
    suspend fun delete(routineSource: RoutineSource)
}