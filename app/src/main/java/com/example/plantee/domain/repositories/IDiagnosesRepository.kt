package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow

interface IDiagnosesRepository {
    fun getDiagnosis(id: Long): Flow<Diagnosis?>

    suspend fun createDiagnosis(diagnosis: Diagnosis): Long
}