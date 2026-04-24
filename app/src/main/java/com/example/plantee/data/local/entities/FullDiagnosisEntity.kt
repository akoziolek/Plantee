package com.example.plantee.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Media

data class FullDiagnosisEntity (
    @Embedded val diagnosis: Diagnosis,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DiagnosisMediaEntity::class,
            parentColumn = "idDiagnosis",
            entityColumn = "idMedia"
        )
    )
    val listOfMedia: List<Media>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RoutineSourceEntity::class,
            parentColumn = "idDiagnosis",
            entityColumn = "idPlantRoutine"
        )
    )
    val plantRoutines: List<PlantRoutineEntity>
)