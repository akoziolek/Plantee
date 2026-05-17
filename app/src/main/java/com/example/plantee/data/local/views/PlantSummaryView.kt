package com.example.plantee.data.local.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    """
    SELECT p.id, p.name, p.description, p.is_favourite, m.id AS media_id, m.file_path AS media_file_path
    FROM plants p
    LEFT JOIN media m ON m.id = p.id_media
    """
)
data class PlantSummaryView(
    val id: Long,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "media_id") val mediaId: Long?,
    @ColumnInfo(name = "media_file_path") val mediaFilePath: String?
)
