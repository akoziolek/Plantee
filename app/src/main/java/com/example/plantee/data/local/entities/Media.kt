package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "media")
data class Media(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "file_name") val fileName: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime
)
