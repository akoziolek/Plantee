package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    fun getMediaForDiagnosis(diagnosisId: Int): Flow<List<Media>>
    fun getMedia(id: Int): Flow<Media?>

    suspend fun createMedia(media: Media): Boolean

    suspend fun updateMedia(media: Media): Boolean

    suspend fun deleteMedia(id: Int): Boolean
}