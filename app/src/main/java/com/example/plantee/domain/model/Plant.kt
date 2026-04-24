package com.example.plantee.domain.model

data class Plant(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val species: String? = null,
    val place: String? = null,
    val state: String? = null,
    val isFavourite: Boolean,
    val mediaId: Long? = null,
    val diagnosesIds: List<Long> = emptyList(),
    val routinesIds: List<Long> = emptyList()
)
