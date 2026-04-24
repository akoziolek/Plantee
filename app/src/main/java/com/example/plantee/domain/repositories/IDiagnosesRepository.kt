package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow

interface IDiagnosesRepository {
    fun getDiagnoses(plantId: Long): Flow<List<Diagnosis>>
    fun getDiagnosis(id: Long): Flow<Diagnosis?>

    suspend fun createDiagnosis(diagnosis: Diagnosis): Long

    suspend fun updateDiagnosis(diagnosis: Diagnosis)

    suspend fun deleteDiagnosis(id: Long)
}