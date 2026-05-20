package com.example.plantee.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.views.DiagnosisRoutineView

data class DiagnosisWithDetails (
    @Embedded val diagnosis: DiagnosisEntity,

    @Relation(
        parentColumn = "id_media",
        entityColumn = "id"
    )
    val media: MediaEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_diagnosis"
    )
    val plantRoutines: List<DiagnosisRoutineView>
)
