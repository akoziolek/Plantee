package com.example.plantee.domain.repositories

import com.example.plantee.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    suspend fun createMedia(media: Media): Long
    suspend fun deleteMedia(id: Long)
}