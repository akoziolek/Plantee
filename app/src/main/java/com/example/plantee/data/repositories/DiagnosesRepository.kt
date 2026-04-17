package com.example.plantee.data.repositories

import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow

class DiagnosesRepository() : IDiagnosesRepository {
    override fun getDiagnoses(ids: List<Int>): Flow<List<Diagnosis>> {
        TODO("Not yet implemented")
    }

    override fun getDiagnosis(id: Int): Flow<Diagnosis?> {
        TODO("Not yet implemented")
    }
}