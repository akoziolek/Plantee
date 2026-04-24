package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlantsRepository (
    private val plantsDao: PlantsDao
) : IPlantsRepository {
    override fun getAllPlantsWithDetails(): Flow<List<Plant>> {
        return plantsDao.getAllFullPlants().map { it.toDomainList() }
    }

    override fun getPlantsWithDetails(ids: List<Int>): Flow<List<Plant>> {
        return plantsDao.getPlantsByIds(ids).map { it.toDomainList() }
    }

    override fun getPlantWithDetails(id: Int): Flow<Plant?> {
        return plantsDao.getFullPlant(id).map { it.toDomain() }
    }

    override suspend fun createPlant(plant: Plant): Boolean {
        val entity = plant.toEntity() ?: return false

        plantsDao.insert(entity)

        return true
    }

    override suspend fun updatePlant(plant: Plant): Boolean {
        val entity = plant.toEntity() ?: return false

        plantsDao.update(entity)
        return true
    }

    override suspend fun deletePlant(id: Int): Boolean {
        plantsDao.deleteById(id)
        return true
    }

}