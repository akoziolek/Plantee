package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlantsRepository @Inject constructor(
    private val plantsDao: PlantsDao
) : IPlantsRepository {
    override fun getAllPlantsWithDetails(): Flow<List<Plant>> {
        return plantsDao.getAllFullPlants().map { it.toDomainList() }
    }

    override fun getPlantsWithDetails(ids: List<Long>): Flow<List<Plant>> {
        return plantsDao.getPlantsByIds(ids).map { it.toDomainList() }
    }

    override fun getPlantWithDetails(id: Long): Flow<Plant?> {
        return plantsDao.getFullPlant(id).map { it.toDomain() }
    }

    override suspend fun createPlant(plant: Plant): Long {
        val entity = plant.toEntity() ?: return -1L

        val newId = plantsDao.insert(entity)

        return newId
    }

    override suspend fun updatePlant(plant: Plant) {
        val entity = plant.toEntity() ?: return

        plantsDao.update(entity)
    }

    override suspend fun deletePlant(id: Long) {
        plantsDao.deleteById(id)
    }

}