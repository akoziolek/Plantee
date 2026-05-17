package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "diagnosis",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_plant"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_media"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["id_media"]),
        Index(value = ["id_plant"])
    ]
)
data class DiagnosisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "problem_description") val problemDescription: String? = null,
    val response: String? = null,
    @ColumnInfo(name = "sun_level") val sunLevel: Int,
    @ColumnInfo(name = "moisture_level") val moistureLevel: Int,
    @ColumnInfo(name = "diagnosed_at") val diagnosedAt: LocalDateTime,
    @ColumnInfo(name = "id_plant") val idPlant: Long,
    @ColumnInfo(name = "id_media") val idMedia: Long? = null
)
