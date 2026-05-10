package com.example.plantee.domain.model

import java.time.LocalDateTime

data class Diagnosis(
    val id: Long = 0L,
    val plantId: Long,
    val problemDescription: String? = null,
    val sunLevel: Int,
    val moistureLevel: Int,
    val diagnosedAt: LocalDateTime,
    val response: String? = null,
    val listOfMedia: List<Media> = emptyList(),
    val routines: List<RoutineSummary> = emptyList()
)
