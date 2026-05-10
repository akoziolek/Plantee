package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.DiagnosisMediaEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity

data class DiagnosisWithDetails (
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
    val plantRoutines: List<PlantEntity>
)