package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.plantee.data.local.dto.PlantSummaryDto
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.views.PlantSummaryView

data class RoutineWithDetails(
    @Embedded val routine: RoutineEntity,

    @Relation(
        entity = PlantSummaryView::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlantRoutineEntity::class,
            parentColumn = "id_routine",
            entityColumn = "id_plant"
        )
    )
    val plants: List<PlantSummaryDto>
)
