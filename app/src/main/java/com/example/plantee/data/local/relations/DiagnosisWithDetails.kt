package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity

data class DiagnosisWithDetails (
    @Embedded val diagnosis: DiagnosisEntity,

    @Relation(
        parentColumn = "id_media",
        entityColumn = "id"
    )
    val media: MediaEntity?,

    @Relation(
        entity = RoutineEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RoutineSourceEntity::class,
            parentColumn = "id_diagnosis",
            entityColumn = "id_plant_routine"
        )
    )
    val plantRoutines: List<RoutineEntity>
)