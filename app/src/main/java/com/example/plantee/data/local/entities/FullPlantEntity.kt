package com.example.plantee.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FullPlantEntity (
    @Embedded val plant: PlantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_plant"
    )
    val plantRoutines: List<PlantRoutineEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_plant"
    )
    val plantDiagnoses: List<DiagnosisEntity>,

    @Relation(
        parentColumn = "id_media",
        entityColumn = "id"
    )
    val media: MediaEntity?
)