package com.example.plantee.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity

data class RoutineWithDetails(
    @Embedded val routine: RoutineEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_routine"
    )
    val plantRoutines: List<PlantRoutineEntity>,

    @ColumnInfo(name = "id_diagnosis")
    val idDiagnosis: Long?,
)
