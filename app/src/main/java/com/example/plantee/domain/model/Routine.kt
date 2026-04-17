package com.example.plantee.domain.model

import java.time.LocalDate

data class Routine(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val activeDays: Int? = null,
    val lastlyDoneAt: LocalDate? = null,
    val plants: List<Plant> = emptyList(),
    val diagnosisId: Int? = null,
    val plantsIds: List<Int> = emptyList()
)
