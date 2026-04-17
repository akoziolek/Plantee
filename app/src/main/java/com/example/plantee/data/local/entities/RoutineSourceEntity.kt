package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_sources",
    foreignKeys = [
        ForeignKey(
            entity = DiagnosisEntity::class,
            parentColumns = ["id"],
            childColumns = ["idDiagnosis"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PlantRoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["idPlantRoutine"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class RoutineSourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_diagnosis") val idDiagnosis: Int,
    @ColumnInfo(name = "id_plant_routine") val idPlantRoutine: Int
)
