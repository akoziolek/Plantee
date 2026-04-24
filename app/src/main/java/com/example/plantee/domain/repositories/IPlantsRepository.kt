package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow
import com.example.plantee.domain.model.Plant

interface IPlantsRepository {
    fun getAllPlantsWithDetails(): Flow<List<Plant>>
    fun getPlantsWithDetails(ids: List<Int>): Flow<List<Plant>>
    fun getPlantWithDetails(id: Int): Flow<Plant?>

    suspend fun createPlant(plant: Plant): Boolean

    suspend fun updatePlant(plant: Plant): Boolean

    suspend fun deletePlant(id: Int): Boolean
}