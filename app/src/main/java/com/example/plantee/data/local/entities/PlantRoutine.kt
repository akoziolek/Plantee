package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_routines",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["idRoutine"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["idPlant"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class PlantRoutine (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_routine") val idRoutine: Int,
    @ColumnInfo(name = "id_plant") val idPlant: Int? = null
)