package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toSummaryDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.data.mappers.toSummaryDomain
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlantsRepository @Inject constructor(
    private val plantsDao: PlantsDao
) : IPlantsRepository {
    override fun getAllPlants(): Flow<List<Plant>> {
        return plantsDao.getAllFullPlants().map { it.toDomainList() }
    }

    override fun getPlants(ids: List<Long>): Flow<List<Plant>> {
        return plantsDao.getPlantsByIds(ids).map { it.toDomainList() }
    }

    override fun getPlant(id: Long): Flow<Plant?> {
        return plantsDao.getFullPlant(id).map { it.toDomain() }
    }

    override fun getPlantSummary(id: Long): Flow<PlantSummary?> {
        return plantsDao.getPlant(id).map { it.toSummaryDomain() }
    }


    // FIXME isn't this ineffective? loading all the data from db just to map it
    override fun getAllPlantsSummary(): Flow<List<PlantSummary>> {
        return plantsDao.getAllPlants().map { it.toSummaryDomainList() }
    }

    override fun getSearchedPlantsSummaryWithSort(
        query: String,
        sort: SortOrder
    ): Flow<List<PlantSummary>> {
        return when (sort) {
            SortOrder.NONE -> {
                plantsDao.searchPlants(query).map { it.toSummaryDomainList() }
            }
            SortOrder.ASCENDING -> {
                plantsDao.searchPlantsAsc(query).map { it.toSummaryDomainList() }
            }
            else -> {
                plantsDao.searchPlantsDesc(query).map { it.toSummaryDomainList() }
            }
        }
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

    override suspend fun togglePlantFavourite(id: Long) {
        plantsDao.updateFavouriteStatus(id)
    }

    override suspend fun deletePlant(id: Long) {
        plantsDao.deleteById(id)
    }

}
