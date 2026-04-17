package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiagnosesRepository(
    private val diagnosisDao: DiagnosisDao
) : IDiagnosesRepository {
    override fun getDiagnoses(plantId: Int): Flow<List<Diagnosis>> {
        return diagnosisDao.getDiagnosesWithDetailsForPlant(plantId).map { it.toDomainList() }
    }

    override fun getDiagnosis(id: Int): Flow<Diagnosis?> {
        return diagnosisDao.getDiagnosisWithDetails(id).map { it.toDomain() }
    }
}