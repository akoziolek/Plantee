package com.example.plantee.domain.model

data class PlantSummary(
    val id: Long,
    val name: String,
    val description: String? = null,
    val isFavourite: Boolean,
    val media: MediaSummary? = null
)
