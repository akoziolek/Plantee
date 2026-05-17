package com.example.plantee.data.local.dto

import androidx.room.Embedded

data class DiagnosisSummaryDto(
    val id: Long,
    val problemDescription: String? = null,

    @Embedded(prefix = "media_")
    val media: MediaSummaryDto? = null
)
