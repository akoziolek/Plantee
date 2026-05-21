package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "routine_sources",
    foreignKeys = [
        ForeignKey(
            entity = DiagnosisEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_diagnosis"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlantRoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_plant_routine"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_diagnosis"]),
        Index(value = ["id_plant_routine"])
    ]
)
data class RoutineSourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "id_diagnosis") val idDiagnosis: Long,
    @ColumnInfo(name = "id_plant_routine") val idPlantRoutine: Long
)
