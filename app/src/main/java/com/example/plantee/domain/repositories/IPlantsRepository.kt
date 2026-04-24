package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow
import com.example.plantee.domain.model.Plant

interface IPlantsRepository {
    fun getAllPlantsWithDetails(): Flow<List<Plant>>
    fun getPlantsWithDetails(ids: List<Long>): Flow<List<Plant>>
    fun getPlantWithDetails(id: Long): Flow<Plant?>

    suspend fun createPlant(plant: Plant): Long

    suspend fun updatePlant(plant: Plant)

    suspend fun deletePlant(id: Long)
}