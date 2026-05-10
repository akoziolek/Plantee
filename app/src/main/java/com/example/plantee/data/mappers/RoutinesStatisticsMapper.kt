package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.RoutinesStatisticsEntity
import com.example.plantee.domain.model.RoutineStatistic

fun RoutinesStatisticsEntity?.toDomain(): RoutineStatistic? {
    if (this == null) return null
     return RoutineStatistic(
        id = this.id,
        currentStreak = this.currentStreak,
         lastStreakUpdate = this.lastStreakUpdate
     )
}

fun RoutineStatistic?.toEntity(): RoutinesStatisticsEntity? {
    if (this == null) return null
    return RoutinesStatisticsEntity (
        id = this.id,
        currentStreak = this.currentStreak,
        lastStreakUpdate = this.lastStreakUpdate
    )
}