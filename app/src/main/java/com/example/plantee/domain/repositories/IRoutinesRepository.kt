package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface IRoutinesRepository {
    fun getAllRoutines(): Flow<List<Routine>>
    fun getTodayRoutines(): Flow<List<Routine>>
    fun getRoutine(id: Int): Flow<Routine?>

    suspend fun addRoutine(routine: Routine): Boolean
    suspend fun updateRoutine(routine: Routine): Boolean
    suspend fun deleteRoutine(id: Int): Boolean
}