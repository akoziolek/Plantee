package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.FullDiagnosisEntity
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Media

fun MediaEntity.toDomain(): Media {
    return Media (
        id = this.id,
        fileName = this.fileName,
        filePath = this.filePath,
        createdAt = this.createdAt
    )
}

fun List<MediaEntity>.toDomainList(): List<Media> {
    return this.map { entity ->
        Media(
            id = entity.id,
            fileName = entity.fileName,
            filePath = entity.filePath,
            createdAt = entity.createdAt
        )
    }
}

fun Media?.toEntity(): MediaEntity? {
    if (this == null) return null

    return MediaEntity(
        id = this.id,
        fileName = this.fileName,
        filePath = this.filePath,
        createdAt = this.createdAt
    )
}