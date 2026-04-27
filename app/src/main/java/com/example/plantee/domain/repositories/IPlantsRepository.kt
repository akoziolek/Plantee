package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.ui.viewmodels.plant.SortOrder
import kotlinx.coroutines.flow.Flow

interface IPlantsRepository {
    fun getAllPlants(): Flow<List<Plant>>
    fun getPlants(ids: List<Long>): Flow<List<Plant>>
    fun getPlant(id: Long): Flow<Plant?>
    fun getAllPlantsSummary(): Flow<List<PlantSummary>>
    fun getSearchedPlantsSummaryWithSort(query: String, sort: SortOrder): Flow<List<PlantSummary>>

    suspend fun createPlant(plant: Plant): Long

    suspend fun updatePlant(plant: Plant)
    suspend fun togglePlantFavourite(id: Long)

    suspend fun deletePlant(id: Long)
}
