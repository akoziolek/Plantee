package com.example.plantee.data.repositories

import androidx.room.withTransaction
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.DiagnosisMediaEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class DiagnosesRepository(
    private val db: AppDatabase,
    private val diagnosisDao: DiagnosisDao,
    private val diagnosisMediaDao: DiagnosisMediaDao,
    private val plantRoutinesDao: PlantRoutinesDao,
    private val routineSourcesDao: RoutineSourcesDao
) : IDiagnosesRepository {
    override fun getDiagnoses(plantId: Int): Flow<List<Diagnosis>> {
        return diagnosisDao.getDiagnosesWithDetailsForPlant(plantId).map { it.toDomainList() }
    }

    override fun getDiagnosis(id: Int): Flow<Diagnosis?> {
        return diagnosisDao.getDiagnosisWithDetails(id).map { it.toDomain() }
    }

    override suspend fun createDiagnosis(diagnosis: Diagnosis): Boolean {
        val entity = diagnosis.toEntity() ?: return false

        db.withTransaction {
            val newId = diagnosisDao.insert(entity)

            if (diagnosis.routinesIds.isNotEmpty()) {
                val ids = plantRoutinesDao.insertAll(diagnosis.routinesIds.map { id ->
                    PlantRoutineEntity(idRoutine = id, idPlant = diagnosis.plantId)
                })

                routineSourcesDao.insertAll(ids.map { id ->
                    RoutineSourceEntity(idDiagnosis = newId, idPlantRoutine = id.toInt())
                })
            }

            if (diagnosis.listOfMedia.isNotEmpty()) {
                diagnosisMediaDao.insertAll(diagnosis.listOfMedia.map { id ->
                    DiagnosisMediaEntity(idDiagnosis = newId, idMedia = id)
                })
            }
        }

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