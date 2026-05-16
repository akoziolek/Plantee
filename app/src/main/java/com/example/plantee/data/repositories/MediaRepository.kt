package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisMediaDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.mappers.toDomain
import com.example.plantee.data.mappers.toDomainList
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.repositories.IMediaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaDao: MediaDao,
    private val diagnosisMediaDao: DiagnosisMediaDao
) : IMediaRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMediaForDiagnosis(diagnosisId: Long): Flow<List<Media>> {
        return diagnosisMediaDao.getMediaIdsForDiagnosis(diagnosisId)
            .flatMapLatest { ids ->
                mediaDao.getMediaByIds(ids)
            }
            .map { entities -> entities.toDomainList() }
    }

    override fun getMedia(id: Long): Flow<Media?> {
        return mediaDao.getMedia(id).map { it.toDomain() }
    }

    override suspend fun createMedia(media: Media): Long {
        val entity = media.toEntity() ?: return -1L

        val newId = mediaDao.insert(entity)

        return newId
    }

    override suspend fun deleteMedia(id: Long) {
        mediaDao.deleteById(id)
    }

}
