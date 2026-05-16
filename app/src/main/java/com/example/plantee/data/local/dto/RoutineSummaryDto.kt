package com.example.plantee.data.local.dto

import androidx.room.ColumnInfo
import java.time.LocalDate

data class RoutineSummaryDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "lastly_done_at") val lastlyDoneAt: LocalDate? = null
)
