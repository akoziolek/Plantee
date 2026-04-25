package com.example.plantee.domain.repositories

import com.example.plantee.data.local.relations.PlantWithDetails
import com.example.plantee.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface IPlantsRepository {
    fun getAllPlants(): Flow<List<Plant>>
    fun getPlants(ids: List<Long>): Flow<List<Plant>>
    fun getPlant(id: Long): Flow<Plant?>

    suspend fun createPlant(plant: Plant): Long

    suspend fun updatePlant(plant: Plant)

    suspend fun deletePlant(id: Long)
}
