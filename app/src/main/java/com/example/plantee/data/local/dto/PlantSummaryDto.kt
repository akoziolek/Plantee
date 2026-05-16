package com.example.plantee.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class PlantSummaryDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,

    @Embedded(prefix = "media_")
    val media: MediaSummaryDto? = null
)
