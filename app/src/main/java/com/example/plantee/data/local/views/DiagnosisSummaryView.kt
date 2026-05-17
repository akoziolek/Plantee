package com.example.plantee.data.local.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.time.LocalDateTime

@DatabaseView(
    """
    SELECT d.id, d.problem_description, d.diagnosed_at, d.id_plant, m.id AS media_id, m.file_path AS media_file_path
    FROM diagnosis d
    LEFT JOIN media m ON m.id = d.id_media
    """
)
data class DiagnosisSummaryView(
    val id: Long,
    @ColumnInfo(name = "problem_description") val problemDescription: String?,
    @ColumnInfo(name = "diagnosed_at") val diagnosedAt: LocalDateTime,
    @ColumnInfo(name = "id_plant") val idPlant: Long,
    @ColumnInfo(name = "media_id") val mediaId: Long?,
    @ColumnInfo(name = "media_file_path") val mediaFilePath: String?
)
