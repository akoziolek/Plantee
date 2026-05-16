package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toDomainListSimple
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.data.mappers.toSummaryDomainList
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.ui.viewmodels.routine.FilterState
import com.example.plantee.utils.RoutineStatus
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class RoutinesRepository @Inject constructor(
    private val routinesDao: RoutinesDao,
    private val plantRoutinesDao: PlantRoutinesDao
) : IRoutinesRepository {
    override fun getAllRoutines(): Flow<List<Routine>> {
        return routinesDao.getAllRoutinesWithDetails().map { it.toDomainList() }
    }

    override fun getTodayRoutines(): Flow<List<Routine>> {
        val today = LocalDate.now()
        return routinesDao.getRoutinesWithDate(today).map { it.toDomainList() }
    }

    override fun getSearchedRoutinesWithSortAndFilterSummary(
        query: String,
        sort: SortOrder,
        filter: FilterState
    ): Flow<List<RoutineSummary>> {
        val isActiveOnly = if (filter.status == RoutineStatus.Active) 1 else 0
        val today = LocalDate.now()

        return when (sort) {
            SortOrder.NONE -> {
                routinesDao.searchRoutines(query, isActiveOnly, today, filter.selectedDays).map { it.toSummaryDomainList() }
            }
            SortOrder.ASCENDING -> {
                routinesDao.searchRoutinesAsc(query, isActiveOnly, today, filter.selectedDays).map { it.toSummaryDomainList() }
            }
            else -> {
                routinesDao.searchRoutinesDesc(query, isActiveOnly, today, filter.selectedDays).map { it.toSummaryDomainList() }
            }
        }
    }

    override fun getRoutinesForWeekdaySummary(weekday: Int): Flow<List<Routine>> {
        val dayBitmap = 1 shl (weekday - 1)
        val today = LocalDate.now()
        return routinesDao.getRoutinesForWeekday(dayBitmap, today).map { it.toDomainListSimple() }
    }


    override fun getRoutine(id: Long): Flow<Routine?> {
        return routinesDao.getRoutineWithDetails(id).map { it.toDomain() }
    }

    override suspend fun addRoutine(routine: Routine): Long {
        val entity = routine.toEntity() ?: return -1L

        val newId =  routinesDao.insert(entity)

        if (routine.plants.isNotEmpty()) {
            plantRoutinesDao.insertAll(routine.plants.map { plant ->
                PlantRoutineEntity(idRoutine = newId, idPlant = plant.id)
            })
        }
        return newId
    }

    override suspend fun updateRoutine(routine: Routine) {
        val entity = routine.toEntity() ?: return

        routinesDao.update(entity)
        plantRoutinesDao.clearAndInsertNewForRoutine(routine.id, routine.plants.map { it.id })
    }

    override suspend fun toggleRoutineDone(id: Long, date: LocalDate?) {
        routinesDao.updateLastlyDoneAt(id, date)
    }


    override suspend fun deleteRoutine(id: Long) {
        routinesDao.deleteById(id)
    }
}