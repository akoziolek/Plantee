package com.example.plantee.domain.model

data class Plant(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val species: String? = null,
    val state: String? = null,
    val isFavourite: Boolean,
    val mediaId: Long? = null,
    //FIXME idk are we keeping id or small objects?? || same for Diagnosis
    // TODO talk about it
    val diagnoses: List<DiagnosisSummary> = emptyList(),
    val routines: List<RoutineSummary> = emptyList()
)
