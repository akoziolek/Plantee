package com.example.plantee.domain.model

import java.time.LocalDateTime

data class Diagnosis(
    val id: Long = 0L,
    val plantId: Long,
    val problemDescription: String? = null,
    val sunLevel: Float,
    val moistureLevel: Float,
    val diagnosedAt: LocalDateTime,
    val response: String? = null,
    val media: Media? = null,
    val routines: List<RoutineSummary> = emptyList()
)
