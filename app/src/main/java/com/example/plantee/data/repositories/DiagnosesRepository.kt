package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.DiagnosisMediaEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class DiagnosesRepository(
    private val diagnosisDao: DiagnosisDao,
    private val diagnosisMediaDao: DiagnosisMediaDao,
    private val routinesDao: RoutinesDao
) : IDiagnosesRepository {
    override fun getDiagnoses(plantId: Int): Flow<List<Diagnosis>> {
        return diagnosisDao.getDiagnosesWithDetailsForPlant(plantId).map { it.toDomainList() }
    }

    override fun getDiagnosis(id: Int): Flow<Diagnosis?> {
        return diagnosisDao.getDiagnosisWithDetails(id).map { it.toDomain() }
    }

    override suspend fun createDiagnosis(diagnosis: Diagnosis): Boolean {
        val entity = diagnosis.toEntity() ?: return false

        val newId =  diagnosisDao.insert(entity)

//        if (diagnosis.routinesIds.isNotEmpty()) {
//            plantRoutinesDao.insertAll(routine.plantsIds.map { id ->
//                PlantRoutineEntity(idRoutine = newId, idPlant = id)
//            })
//        }

        if (diagnosis.listOfMedia.isNotEmpty()) {
            diagnosisMediaDao.insertAll(diagnosis.listOfMedia.map { id ->
                DiagnosisMediaEntity(idDiagnosis = newId, idMedia = id)
            })
        }

        TODO("Add routines for diagnosis")

        return true
    }

    override suspend fun updateDiagnosis(diagnosis: Diagnosis): Boolean {
        // TODO("Do we need to update diagnosis? What can be updated?")
        val entity = diagnosis.toEntity() ?: return false

        diagnosisDao.update(entity)
        return true
    }

    override suspend fun deleteDiagnosis(id: Int): Boolean {
        diagnosisDao.deleteById(id)
        return true
    }
}