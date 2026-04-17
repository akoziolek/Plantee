package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow
import com.example.plantee.domain.model.Plant

interface IPlantsRepository {
    fun getAllPlants(): Flow<List<Plant>>
    fun getPlant(id: Int): Flow<Plant?>
}