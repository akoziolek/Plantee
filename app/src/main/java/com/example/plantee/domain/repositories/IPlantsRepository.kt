package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.example.plantee.domain.model.Plant

interface IPlantsRepository {
    fun getAllPlantsWithDetails(): Flow<List<Plant>>
    fun getPlantsWithDetails(ids: List<Int>): Flow<List<Plant>>
    fun getPlantWithDetails(id: Int): Flow<Plant?>

    // TODO("cud fun")
}