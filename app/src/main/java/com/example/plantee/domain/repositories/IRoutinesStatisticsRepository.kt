package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IRoutinesStatisticsRepository {
    fun getEffectiveStreak(today: LocalDate): Flow<Int>
    suspend fun syncStreak()
}