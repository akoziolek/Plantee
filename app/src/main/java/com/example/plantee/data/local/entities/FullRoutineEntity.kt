package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class FullRoutineEntity(
    @Embedded val routine: RoutineEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_routine"
    )
    val plantRoutines: List<PlantRoutineEntity>,

    @ColumnInfo(name = "id_diagnosis")
    val idDiagnosis: Long?,
)
