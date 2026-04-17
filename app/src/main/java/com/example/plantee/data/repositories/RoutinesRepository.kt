package com.example.plantee.data.repositories

import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IRoutinesRepository
import kotlinx.coroutines.flow.Flow

class RoutinesRepository() : IRoutinesRepository {
    override fun getAllRoutines(): Flow<List<Routine>> {
        TODO("Not yet implemented")
    }

    override fun getTodayRoutines(): Flow<List<Routine>> {
        TODO("Not yet implemented")
    }

    override fun getRoutine(id: Int): Flow<Routine?> {
        TODO("Not yet implemented")
    }
}