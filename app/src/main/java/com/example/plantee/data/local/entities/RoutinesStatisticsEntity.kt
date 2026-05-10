package com.example.plantee.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "routines_statistics")
data class RoutinesStatisticsEntity (
    @PrimaryKey val id: Long = 0L,
    val currentStreak: Int = 0,
    val lastStreakUpdate: LocalDate? = null
)