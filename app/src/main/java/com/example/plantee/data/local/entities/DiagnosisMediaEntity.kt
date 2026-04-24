package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "diagnosis_media",
    foreignKeys = [
        ForeignKey(
            entity = DiagnosisEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_diagnosis"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_media"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_diagnosis"]),
        Index(value = ["id_media"])
    ]
)
data class DiagnosisMediaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_diagnosis") val idDiagnosis: Int,
    @ColumnInfo(name = "id_media") val idMedia: Int
)
