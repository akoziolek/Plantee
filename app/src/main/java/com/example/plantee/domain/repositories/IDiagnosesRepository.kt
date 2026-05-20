package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface IDiagnosesRepository {
    fun getDiagnosis(id: Long): Flow<Diagnosis?>

    suspend fun createDiagnosis(diagnosis: Diagnosis, routines: List<Routine>): Long
    suspend fun associateRoutineWithDiagnosis(diagnosisId: Long, routineId: Long)
}