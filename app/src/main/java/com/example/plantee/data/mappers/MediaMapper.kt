package com.example.plantee.data.mappers

import com.example.plantee.data.local.dto.MediaSummaryDto
import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.MediaSummary

fun MediaEntity?.toDomain(): Media? {
    if (this == null) return null

    return Media (
        id = this.id,
        fileName = this.fileName,
        filePath = this.filePath,
        createdAt = this.createdAt
    )
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

fun MediaSummaryDto?.toDomain(): MediaSummary? {
    if (this == null) return null

    return MediaSummary (
        id = this.id,
        filePath = this.filePath
    )
}