package com.example.plantee.data.repositories

import androidx.room.withTransaction
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.data.mappers.toSummaryDomainList
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlantsRepository @Inject constructor(
    private val database: AppDatabase,
    private val plantsDao: PlantsDao,
    private val mediaDao: MediaDao
) : IPlantsRepository {
    override fun getPlant(id: Long): Flow<Plant?> {
        return plantsDao.getFullPlant(id).map { it.toDomain() }
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

    override suspend fun createPlantWithMedia(plant: Plant, media: Media?): Long {
        return database.withTransaction {
            val savedMediaId = media?.let {
                mediaDao.insert(it.toEntity() ?: return@withTransaction -1L)
            }

            val plantEntity = plant.toEntity()?.copy(
                idMedia = savedMediaId
            ) ?: return@withTransaction -1L

            plantsDao.insert(plantEntity)
        }
    }

    override suspend fun updatePlant(plant: Plant) {
        val entity = plant.toEntity() ?: return

        plantsDao.update(entity)
    }

    override suspend fun updatePlantMedia(id: Long, media: Media?) {
        database.withTransaction {
            // FIXME firstOrNull??
            val plant = getPlant(id).firstOrNull() ?: return@withTransaction

            val oldMediaId = plant.media?.id
            if (oldMediaId != null) {
                mediaDao.deleteById(oldMediaId)
            }

            val newMediaId = media?.let {
                val entity = it.toEntity() ?: return@withTransaction
                mediaDao.insert(entity)
            }

            plantsDao.updateMediaId(id, newMediaId)
        }
    }

    override suspend fun togglePlantFavourite(id: Long) {
        plantsDao.updateFavouriteStatus(id)
    }

    override suspend fun deletePlant(id: Long) {
        plantsDao.deleteById(id)
    }

}
