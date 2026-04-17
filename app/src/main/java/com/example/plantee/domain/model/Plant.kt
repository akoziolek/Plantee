package com.example.plantee.domain.model

data class Plant(
    val id: Int,
    val name: String,
    val description: String? = null,
    val species: String? = null,
    val place: String? = null,
    val state: String? = null,
    val isFavourite: Boolean,
    val media: Media? = null,
    val diagnosesIds: List<Int> = emptyList(),
    val routinesIds: List<Int> = emptyList()
)
