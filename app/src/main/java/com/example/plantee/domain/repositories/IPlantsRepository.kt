package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface IPlantsRepository {
    fun getPlant(id: Long): Flow<Plant?>
    fun getSearchedPlantsSummaryWithSort(query: String, sort: SortOrder): Flow<List<PlantSummary>>
    suspend fun createPlantWithMedia(plant: Plant, media: Media?): Long
    suspend fun updatePlantMedia(id: Long, media: Media?) // TODO check if used
    suspend fun updatePlant(plant: Plant)
    suspend fun togglePlantFavourite(id: Long)
    suspend fun deletePlant(id: Long)
}
