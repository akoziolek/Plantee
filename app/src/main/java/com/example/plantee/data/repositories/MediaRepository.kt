package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.local.entities.PlantRoutineEntity
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.repositories.IMediaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class MediaRepository(
    private val mediaDao: MediaDao,
    private val diagnosisMediaDao: DiagnosisMediaDao
) : IMediaRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMediaForDiagnosis(diagnosisId: Int): Flow<List<Media>> {
        return diagnosisMediaDao.getMediaIdsForDiagnosis(diagnosisId)
            .flatMapLatest { ids ->
                mediaDao.getMediaByIds(ids)
            }
            .map { entities -> entities.toDomainList() }
    }

    override fun getMedia(id: Int): Flow<Media?> {
        return mediaDao.getMedia(id).map { it.toDomain() }
    }

    override suspend fun createMedia(media: Media): Boolean {
        val entity = media.toEntity() ?: return false

        mediaDao.insert(entity)

        return true
    }

    override suspend fun updateMedia(media: Media): Boolean {
        // TODO("Do we need to update media? What can be updated?")
        val entity = media.toEntity() ?: return false

        mediaDao.update(entity)
        return true
    }

    override suspend fun deleteMedia(id: Int): Boolean {
        mediaDao.deleteById(id)
        return true
    }

}
