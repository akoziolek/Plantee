package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlantsRepository (
    private val plantsDao: PlantsDao,
    private val plantRoutinesDao: PlantRoutinesDao,
    private val diagnosisDao: DiagnosisDao,
    private val mediaDao: MediaDao
) : IPlantsRepository {
    override fun getAllPlants(): Flow<List<Plant>> {
        return plantsDao.getAllFullPlants().map { it.toDomainList() }
    }

    override fun getPlant(id: Int): Flow<Plant?> {
        return plantsDao.getFullPlant(id).map { it.toDomain() }
    }

}