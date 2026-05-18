package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.DiagnosisDao
import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.data.mappers.toEntity
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.repositories.IMediaRepository
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaDao: MediaDao
) : IMediaRepository {

    override suspend fun createMedia(media: Media): Long {
        val entity = media.toEntity() ?: return -1L

        val newId = mediaDao.insert(entity)

        return newId
    }

    override suspend fun deleteMedia(id: Long) {
        mediaDao.deleteById(id)
    }

}