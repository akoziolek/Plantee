package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.dao.RoutinesStatisticsDao
import com.example.plantee.data.local.entities.RoutinesStatisticsEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.data.mappers.toSummaryDomainList
import com.example.plantee.domain.model.RoutineStatistic
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IRoutinesStatisticsRepository
import com.example.plantee.utils.toDayBitMask
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoutinesStatisticsRepository @Inject constructor(
    private val routinesStatisticsDao: RoutinesStatisticsDao,
    private val routinesDao: RoutinesDao
) : IRoutinesStatisticsRepository {

    override suspend fun syncStreak() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val stats = routinesStatisticsDao.getRoutinesStatistics()?.toDomain()

        if (stats == null) {
            routinesStatisticsDao.upsertRoutineStatistic(
                RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 0)
            )
            return
        }

        val lastUpdate = stats.lastStreakUpdate ?: yesterday
        if (lastUpdate >= yesterday) {
            return
        }

        var currentCheckDate = lastUpdate.plusDays(1)
        var currentStreak = stats.currentStreak

        while (!currentCheckDate.isAfter(yesterday)) {
            val requiredRoutines = routinesDao.getRoutinesRequiredForDate(
                currentCheckDate,
                currentCheckDate.toDayMask()
            ).first().toDomainList()

            val allDone = requiredRoutines.areAllCompletedOn(currentCheckDate)
            if (allDone) {
                currentStreak += 1
            } else {
                if (requiredRoutines.isNotEmpty()) {
                    currentStreak = 0
                }
            }
            currentCheckDate = currentCheckDate.plusDays(1)
        }
        updateStats(RoutineStatistic(currentStreak = currentStreak, lastStreakUpdate = yesterday))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEffectiveStreak(): Flow<Int> {
        return routinesStatisticsDao.getRoutinesStatisticsFlow().flatMapLatest { statEntity ->
            val today = LocalDate.now()
            val stat = statEntity?.toDomain() ?: RoutineStatistic(currentStreak = 0, lastStreakUpdate = today.minusDays(1))

            routinesDao.getRoutinesRequiredForDate(today, today.toDayMask()).map { todayEntities ->
                val todayRoutines = todayEntities.toDomainList()
                val isDoneToday = todayRoutines.areAllCompletedOn(today)

                if (isDoneToday) stat.currentStreak + 1 else stat.currentStreak
            }
        }.distinctUntilChanged()
    }

    private suspend fun updateStats(stats: RoutineStatistic) {
        val entity = stats.toEntity() ?: return
        routinesStatisticsDao.upsertRoutineStatistic(entity)
    }

    private fun List<Routine>.areAllCompletedOn(date: LocalDate): Boolean {
        return this.isNotEmpty() && this.all { it.lastlyDoneAt == date }
    }
}