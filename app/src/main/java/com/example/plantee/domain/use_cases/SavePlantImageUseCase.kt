package com.example.plantee.domain.use_cases

import android.net.Uri
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.repositories.IMediaRepository
import com.example.plantee.domain.repositories.IPhotosRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SavePlantImageUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository,
    private val mediaRepository: IMediaRepository
) {
    suspend operator fun invoke(newImageUri: Uri, oldMedia: Media? = null): Media? {
        val newFilePath = photosRepository.saveImage(newImageUri) ?: return null

        return try {
            val media = Media(
                filePath = newFilePath,
                createdAt = LocalDateTime.now()
            )
            val mediaId = mediaRepository.createMedia(media)
            val savedMedia = media.copy(id = mediaId)

            oldMedia?.let { old ->
                photosRepository.deleteImage(old.filePath)
                mediaRepository.deleteMedia(old.id)
            }

            savedMedia
        } catch (e: Exception) {
            photosRepository.deleteImage(newFilePath)
            null
        }
    }
}
