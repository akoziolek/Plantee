package com.example.plantee.domain.model

import java.time.LocalDate

data class RoutineSummary(
    val id: Long,
    val name: String,
    val description: String? = null,
    val lastlyDoneAt: LocalDate? = null
)
