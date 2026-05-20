package com.example.plantee.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AiProposedRoutine(
    val tempId: Long,
    val name: String,
    val description: String,
    val activeDays: Int,
    val startDate: String?,
    val endDate: String?
)


@Serializable
data class AiDiagnosisResult(
    val isPlantRelated: Boolean,
    val diagnosisDescription: String,
    val proposedRoutines: List<AiProposedRoutine>
)
