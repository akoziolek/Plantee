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

    override fun getSearchedRoutinesWithSortSummary(
        query: String,
        sort: SortOrder
    ): Flow<List<RoutineSummary>> {
        return when (sort) {
            SortOrder.NONE -> {
                routinesDao.searchRoutines(query).map { it.toSummaryDomainList() }
            }
            SortOrder.ASCENDING -> {
                routinesDao.searchRoutinesAsc(query).map { it.toSummaryDomainList() }
            }
            else -> {
                routinesDao.searchRoutinesDesc(query).map { it.toSummaryDomainList() }
            }
        }
    }

    override fun getRoutinesForWeekdaySummary(weekday: Int): Flow<List<Routine>> {
        val dayBitmap = when (weekday) {
            1 -> 1
            2 -> 2
            3 -> 4
            4 -> 8
            5 -> 16
            6 -> 32
            7 -> 64
            else -> 0
        }
        return routinesDao.getRoutinesForWeekday(dayBitmap).map { it.toDomainListSimple() }
    }


    override fun getRoutine(id: Long): Flow<Routine?> {
        return routinesDao.getRoutineWithDetails(id).map { it.toDomain() }
    }

    override suspend fun addRoutine(routine: Routine): Long {
        val entity = routine.toEntity() ?: return -1L

        val newId =  routinesDao.insert(entity)

        if (routine.plantsIds.isNotEmpty()) {
            plantRoutinesDao.insertAll(routine.plantsIds.map { id ->
                PlantRoutineEntity(idRoutine = newId, idPlant = id)
            })
        }
        return newId
    }

    override suspend fun updateRoutine(routine: Routine) {
        val entity = routine.toEntity() ?: return

        routinesDao.update(entity)
        plantRoutinesDao.clearAndInsertNewForRoutine(routine.id, routine.plantsIds)
    }

    override suspend fun toggleRoutineDone(id: Long) {
        routinesDao.updateLastlyDoneAt(id)
    }


    override suspend fun deleteRoutine(id: Long) {
        routinesDao.deleteById(id)
    }
}