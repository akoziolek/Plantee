package com.example.plantee.domain.model

import java.time.LocalDateTime

data class Media(
    val id: Long = 0L,
    val filePath: String,
    val fileName: String? = null,
    val createdAt: LocalDateTime
)
