package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "plants_media",
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["idPlant"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Media::class,
            parentColumns = ["id"],
            childColumns = ["idMedia"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class PlantsMedia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_plant") val idPlant: Int,
    @ColumnInfo(name = "id_media") val idMedia: Int
)
