package com.example.plantee.domain.model

import java.time.LocalDate

data class Routine(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val activeDays: Int? = null,
    val lastlyDoneAt: LocalDate? = null,
    val diagnosisId: Long? = null,
    val plantsIds: List<Long> = emptyList()
)
