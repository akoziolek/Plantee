package com.example.plantee.domain.model

import java.time.LocalDateTime

data class DiagnosisSummary(
    val id: Long,
    val diagnosedAt: LocalDateTime,
    val description: String? = null
)
