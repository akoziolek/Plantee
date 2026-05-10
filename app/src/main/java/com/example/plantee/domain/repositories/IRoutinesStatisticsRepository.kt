package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow

interface IRoutinesStatisticsRepository {
    fun getEffectiveStreak(): Flow<Int>
    suspend fun syncStreak()
}