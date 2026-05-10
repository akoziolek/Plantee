package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.plantee.data.local.entities.RoutinesStatisticsEntity
import com.example.plantee.domain.model.RoutineStatistic
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutinesStatisticsDao {
    @Query("SELECT * FROM routines_statistics LIMIT 1")
    fun getRoutinesStatisticsFlow(): Flow<RoutinesStatisticsEntity?>

    @Query("SELECT * FROM routines_statistics LIMIT 1")
    suspend fun getRoutinesStatistics(): RoutinesStatisticsEntity?

    @Upsert
    suspend fun upsertRoutineStatistic(routineStatistic: RoutinesStatisticsEntity)
}