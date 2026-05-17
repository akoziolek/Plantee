package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.plantee.data.local.entities.RoutineSourceEntity

@Dao
interface RoutineSourcesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(routineSource: List<RoutineSourceEntity>): List<Long>
}