package com.example.plantee.domain.model

data class PlantSummary(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val isFavourite: Boolean,
    val media: Media? = null
)
