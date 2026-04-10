package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "diagnosis_media",
    foreignKeys = [
        ForeignKey(
            entity = Diagnosis::class,
            parentColumns = ["id"],
            childColumns = ["idDiagnosis"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Media::class,
            parentColumns = ["id"],
            childColumns = ["idMedia"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class DiagnosisMedia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_diagnosis") val idDiagnosis: Int,
    @ColumnInfo(name = "id_media") val idMedia: Int
)
