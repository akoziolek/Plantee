package com.example.plantee.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import java.time.LocalDateTime

data class DiagnosisSummaryDto(
    val id: Long,
    @ColumnInfo(name = "problem_description") val problemDescription: String? = null,
    @ColumnInfo(name = "diagnosed_at") val diagnosedAt: LocalDateTime,

    @Embedded(prefix = "media_")
    val media: MediaSummaryDto? = null
)
