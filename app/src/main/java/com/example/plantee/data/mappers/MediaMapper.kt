package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.MediaEntity
import com.example.plantee.domain.model.Media

fun MediaEntity.toDomain(): Media {
    return Media (
        id = this.id,
        fileName = this.fileName,
        filePath = this.filePath,
        createdAt = this.createdAt
    )
}