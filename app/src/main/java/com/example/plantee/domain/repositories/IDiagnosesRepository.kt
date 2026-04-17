package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Diagnosis
import kotlinx.coroutines.flow.Flow

interface IDiagnosesRepository {
    fun getDiagnoses(ids: List<Int>): Flow<List<Diagnosis>>
    fun getDiagnosis(id: Int): Flow<Diagnosis?>
}