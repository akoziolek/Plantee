package com.example.plantee.domain.model

import java.time.LocalDateTime

data class Diagnosis(
    val id: Int = 0,
    val description: String? = null,
    val sunLevel: Int,
    val moistureLevel: Int,
    val diagnosedAt: LocalDateTime,
    val listOfMedia: List<Media> = emptyList(),
    val routinesIds: List<Int> = emptyList()
)
