package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface IRoutinesRepository {
    fun getAllRoutines(): Flow<List<Routine>>
    fun getTodayRoutines(): Flow<List<Routine>>
    fun getSearchedRoutinesWithSortSummary(query: String, sort: SortOrder): Flow<List<RoutineSummary>>
    fun getRoutinesForWeekdaySummary(weekday: Int): Flow<List<Routine>>
    fun getRoutine(id: Long): Flow<Routine?>

    suspend fun addRoutine(routine: Routine): Long
    suspend fun updateRoutine(routine: Routine)
    suspend fun toggleRoutineDone(id: Long)
    suspend fun deleteRoutine(id: Long)
}