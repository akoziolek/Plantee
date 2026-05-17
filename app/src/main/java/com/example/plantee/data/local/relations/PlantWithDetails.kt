package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.plantee.data.local.dto.DiagnosisSummaryDto
import com.example.plantee.data.local.dto.RoutineSummaryDto
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.views.DiagnosisSummaryView

data class PlantWithDetails (
    @Embedded val plant: PlantEntity,

    @Relation(
        entity = RoutineEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlantRoutineEntity::class,
            parentColumn = "id_plant",
            entityColumn = "id_routine"
        )
    )
    val plantRoutines: List<RoutineSummaryDto>,

    @Relation(
        entity = DiagnosisSummaryView::class,
        parentColumn = "id",
        entityColumn = "id_plant"
    )
    val plantDiagnoses: List<DiagnosisSummaryDto>,

    @Relation(
        parentColumn = "id_media",
        entityColumn = "id"
    )
    val media: MediaEntity?
)
