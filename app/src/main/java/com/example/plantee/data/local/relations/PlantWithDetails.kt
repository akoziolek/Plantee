package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity

data class PlantWithDetails (
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