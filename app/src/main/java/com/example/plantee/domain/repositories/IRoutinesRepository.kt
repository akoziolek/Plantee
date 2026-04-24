package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface IRoutinesRepository {
    fun getAllRoutines(): Flow<List<Routine>>
    fun getTodayRoutines(): Flow<List<Routine>>
    fun getRoutine(id: Long): Flow<Routine?>

    suspend fun addRoutine(routine: Routine): Long
    suspend fun updateRoutine(routine: Routine)
    suspend fun deleteRoutine(id: Long)
}