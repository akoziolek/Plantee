package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IRoutinesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoutinesRepository(
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

    override fun getRoutine(id: Int): Flow<Routine?> {
        return routinesDao.getRoutineWithDetails(id).map { it.toDomain() }
    }

    override suspend fun addRoutine(routine: Routine): Boolean {
        val entity = routine.toEntity() ?: return false

        // FIXME Insert return type
        val newId =  routinesDao.insert(entity).toInt()

        if (routine.plantsIds.isNotEmpty()) {
            plantRoutinesDao.insertAll(routine.plantsIds.map { id ->
                PlantRoutineEntity(idRoutine = newId, idPlant = id)
            })
        }
        return true
    }

    override suspend fun updateRoutine(routine: Routine): Boolean {
        val entity = routine.toEntity() ?: return false

        routinesDao.update(entity)
        plantRoutinesDao.clearAndInsertNewForRoutine(routine.id, routine.plantsIds)
        return true
    }

    override suspend fun deleteRoutine(id: Int): Boolean {
        routinesDao.deleteById(id)
        return true
    }
}