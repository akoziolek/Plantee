package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.ui.viewmodels.routine.FilterState
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IRoutinesRepository {
    fun getRoutinesForDay(date: LocalDate = LocalDate.now()): Flow<List<RoutineSummary>>
    fun getSearchedRoutinesWithSortAndFilterSummary(query: String, sort: SortOrder, filter: FilterState): Flow<List<RoutineSummary>>
    fun getRoutine(id: Long): Flow<Routine?>
    suspend fun addRoutine(routine: Routine): Long
    suspend fun updateRoutine(routine: Routine)
    suspend fun toggleRoutineDone(id: Long, date: LocalDate?)
    suspend fun deleteRoutine(id: Long)
}