package com.example.plantee.data.local.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    """
    SELECT r.id, r.name, r.description, rs.id_diagnosis
    FROM routines r
    JOIN plant_routines pr ON r.id = pr.id_routine
    JOIN routine_sources rs ON pr.id = rs.id_plant_routine
    """
)
data class DiagnosisRoutineView(
    val id: Long,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "id_diagnosis") val idDiagnosis: Long
)
