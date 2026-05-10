package com.example.plantee.domain.model

data class Plant(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val species: String? = null,
    val state: String? = null,
    val isFavourite: Boolean,
    val media: Media? = null,
    val diagnoses: List<DiagnosisSummary> = emptyList(),
    val routines: List<RoutineSummary> = emptyList()
)
