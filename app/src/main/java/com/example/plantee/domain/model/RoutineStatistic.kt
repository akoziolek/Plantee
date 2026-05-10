package com.example.plantee.domain.model

import java.time.LocalDate

data class RoutineStatistic (
    val id: Long = 0,
    val currentStreak: Int = 0,
    val lastStreakUpdate: LocalDate? = null
)