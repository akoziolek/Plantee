package com.example.plantee.data.local.dto

import androidx.room.ColumnInfo

data class MediaSummaryDto(
    val id: Long,
    @ColumnInfo(name = "file_path") val filePath: String
)
