package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    fun getMediaForDiagnosis(diagnosisId: Long): Flow<List<Media>>
    fun getMedia(id: Long): Flow<Media?>

    suspend fun createMedia(media: Media): Long
    suspend fun deleteMedia(id: Long)
}