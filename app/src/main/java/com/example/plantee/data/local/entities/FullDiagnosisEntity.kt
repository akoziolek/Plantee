package com.example.plantee.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FullDiagnosisEntity (
    @Embedded val diagnosis: DiagnosisEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DiagnosisMediaEntity::class,
            parentColumn = "id_diagnosis",
            entityColumn = "id_media"
        )
    )
    val listOfMedia: List<MediaEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RoutineSourceEntity::class,
            parentColumn = "id_diagnosis",
            entityColumn = "id_plant_routine"
        )
    )
    val plantRoutines: List<PlantRoutineEntity>
)