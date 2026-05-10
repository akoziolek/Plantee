package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.dao.RoutinesStatisticsDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineStatistic
import com.example.plantee.domain.repositories.IRoutinesStatisticsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class RoutinesStatisticsRepository @Inject constructor(
    private val routinesStatisticsDao: RoutinesStatisticsDao,
    private val routinesDao: RoutinesDao
) : IRoutinesStatisticsRepository {

    override suspend fun syncStreak() {
        val stats = routinesStatisticsDao.getRoutinesStatistics()?.toDomain() ?: return
        val lastUpdate = stats.lastStreakUpdate ?: return
        val yesterday = LocalDate.now().minusDays(1)

        if (!lastUpdate.isBefore(LocalDate.now())) return

        val yesterdayRoutines = routinesDao.getRoutinesWithDate(yesterday).first().toDomainList()
        val allDoneYesterday = yesterdayRoutines.areAllCompletedOn(yesterday)

        if (allDoneYesterday) {
            updateStats(stats.copy(currentStreak = stats.currentStreak + 1, lastStreakUpdate = yesterday))
        } else {
            if (!isStreakValid(lastUpdate, yesterday)) {
                updateStats(stats.copy(currentStreak = 0))
            }
        }
    }

    override fun getEffectiveStreak(): Flow<Int> {
        val today = LocalDate.now()

        return combine(
            routinesStatisticsDao.getRoutinesStatisticsFlow(),
            routinesDao.getRoutinesRequiredForDate(today, today.toDayMask())
        ) { statEntity, todayEntities ->
            val stat = statEntity?.toDomain() ?: return@combine 0
            val todayRoutines = todayEntities.toDomainList()

            val isDoneToday = todayRoutines.areAllCompletedOn(today)
            val lastUpdate = stat.lastStreakUpdate ?: return@combine 0

            if (lastUpdate == today) {
                if (!isDoneToday) maxOf(0, stat.currentStreak - 1) else stat.currentStreak
            } else {
                val baseStreak = if (isStreakValid(lastUpdate, today.minusDays(1))) stat.currentStreak else 0
                if (isDoneToday) baseStreak + 1 else baseStreak
            }
        }.distinctUntilChanged()
    }


    private suspend fun updateStats(stats: RoutineStatistic) {
        val entity = stats.toEntity() ?: return
        routinesStatisticsDao.upsertRoutineStatistic(entity)
    }

    private suspend fun isStreakValid(lastUpdate: LocalDate?, endDate: LocalDate): Boolean {
        if (lastUpdate == null) return false
        if (!lastUpdate.isBefore(endDate)) return true

        return !checkIfTasksWereRequiredBetween(lastUpdate.plusDays(1), endDate)
    }

    private suspend fun checkIfTasksWereRequiredBetween(startDate: LocalDate, endDate: LocalDate): Boolean {
        val allRoutines = routinesDao.getRoutinesActiveInPeriod(startDate, endDate)
        if (allRoutines.isEmpty()) return false

        var currentDay = startDate
        while (!currentDay.isAfter(endDate)) {
            val dayMask = currentDay.toDayMask()

            val taskRequiredThatDay = allRoutines.any { routine ->
                val isActive = (routine.startDate == null || !currentDay.isBefore(routine.startDate)) &&
                        (routine.endDate == null || !currentDay.isAfter(routine.endDate))

                val isScheduled = routine.activeDays?.let { (it and dayMask) != 0 } ?: false
                isActive && isScheduled
            }

            if (taskRequiredThatDay) return true

            currentDay = currentDay.plusDays(1)
        }
        return false
    }

    private fun LocalDate.toDayMask(): Int = 1 shl (this.dayOfWeek.value - 1)

    private fun List<Routine>.areAllCompletedOn(date: LocalDate): Boolean {
        return this.isNotEmpty() && this.all { it.lastlyDoneAt == date }
    }
}