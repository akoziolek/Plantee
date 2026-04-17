package com.example.plantee.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FullPlantEntity (
    @Embedded val plant: PlantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPlant"
    )
    val plantRoutines: List<PlantRoutineEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "idPlant"
    )
    val plantDiagnoses: List<DiagnosisEntity>,

    @Relation(
        parentColumn = "idMedia",
        entityColumn = "id"
    )
    val media: MediaEntity?
)