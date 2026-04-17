package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow

interface IDiagnosesRepository {
    fun getDiagnoses(plantId: Int): Flow<List<Diagnosis>>
    fun getDiagnosis(id: Int): Flow<Diagnosis?>
}