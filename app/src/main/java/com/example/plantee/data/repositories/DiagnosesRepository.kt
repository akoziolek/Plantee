package com.example.plantee.data.repositories

import androidx.room.withTransaction
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiagnosesRepository @Inject constructor(
    private val db: AppDatabase,
    private val diagnosisDao: DiagnosisDao,
    private val mediaDao: MediaDao,
    private val plantRoutinesDao: PlantRoutinesDao,
    private val routineSourcesDao: RoutineSourcesDao
) : IDiagnosesRepository {
    override fun getDiagnosis(id: Long): Flow<Diagnosis?> {
        return diagnosisDao.getDiagnosisWithDetails(id).map { it.toDomain() }
    }

    override suspend fun createDiagnosis(diagnosis: Diagnosis): Long {
        var newId = -1L
        val entity = diagnosis.toEntity() ?: return newId

        db.withTransaction {
            newId = diagnosisDao.insert(entity)

            if (diagnosis.routines.isNotEmpty()) {
                val ids = plantRoutinesDao.insertAll(diagnosis.routines.map { routine ->
                    PlantRoutineEntity(idRoutine = routine.id, idPlant = diagnosis.plantId)
                })

                routineSourcesDao.insertAll(ids.map { id ->
                    RoutineSourceEntity(idDiagnosis = newId, idPlantRoutine = id)
                })
            }

            if (diagnosis.media != null) {
                mediaDao.insert(
                    MediaEntity(
                        id = diagnosis.media.id,
                        filePath = diagnosis.media.filePath,
                        fileName = diagnosis.media.fileName,
                        createdAt = diagnosis.media.createdAt
                    )
                )
            }
        }

        return newId
    }

    override suspend fun updateDiagnosis(diagnosis: Diagnosis) {
        // TODO("Do we need to update diagnosis? What can be updated?")
        val entity = diagnosis.toEntity() ?: return

        diagnosisDao.update(entity)
    }

    override suspend fun deleteDiagnosis(id: Long) {
        diagnosisDao.deleteById(id)
    }
}