package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IRoutinesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoutinesRepository(
    private val routinesDao: RoutinesDao,
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
}