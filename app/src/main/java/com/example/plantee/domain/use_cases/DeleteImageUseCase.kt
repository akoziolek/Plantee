package com.example.plantee.domain.use_cases

import com.example.plantee.domain.model.Media
import com.example.plantee.domain.repositories.IMediaRepository
import com.example.plantee.domain.repositories.IPhotosRepository
import javax.inject.Inject

class DeleteMediaUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository,
    private val mediaRepository: IMediaRepository
) {
    suspend operator fun invoke(media: Media) {
        photosRepository.deleteImage(media.filePath)
        mediaRepository.deleteMedia(media.id)
    }
}