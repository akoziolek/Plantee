package com.example.plantee.data.repositories

import androidx.room.withTransaction
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutineSourcesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.local.entities.RoutineSourceEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IDiagnosesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiagnosesRepository @Inject constructor(
    private val db: AppDatabase,
    private val diagnosisDao: DiagnosisDao,
    private val mediaDao: MediaDao,
    private val plantRoutinesDao: PlantRoutinesDao,
    private val routineSourcesDao: RoutineSourcesDao,
    private val routinesDao: RoutinesDao
) : IDiagnosesRepository {
    override fun getDiagnosis(id: Long): Flow<Diagnosis?> {
        return diagnosisDao.getDiagnosisWithDetails(id).map { it.toDomain() }
    }

    override suspend fun createDiagnosis(
        diagnosis: Diagnosis,
        routines: List<Routine>
    ): Long {
        var diagnosisId = -1L

        db.withTransaction {
            diagnosis.media?.let { media ->
                val mediaEntity = media.toEntity()
                if(mediaEntity != null)  {
                    mediaDao.insert(mediaEntity)
                }
            }

            val diagnosisEntity = diagnosis.toEntity() ?: return@withTransaction
            diagnosisId = diagnosisDao.insert(diagnosisEntity)

            routines.forEach { routine ->
                val routineEntity = routine.toEntity() ?: return@withTransaction
                val newRoutineId = routinesDao.insert(routineEntity)

                if (newRoutineId != -1L) {
                    val plantRoutineId = plantRoutinesDao.insert(
                        PlantRoutineEntity(idRoutine = newRoutineId, idPlant = diagnosis.plantId)
                    )

                    routineSourcesDao.insert(
                        RoutineSourceEntity(idDiagnosis = diagnosisId, idPlantRoutine = plantRoutineId)
                    )
                }
            }
        }

        return diagnosisId
    }

    override suspend fun associateRoutineWithDiagnosis(diagnosisId: Long, routineId: Long) {
        routineSourcesDao.insert(
            RoutineSourceEntity(
                idDiagnosis = diagnosisId,
                idPlantRoutine = routineId
            )
        )
    }

}