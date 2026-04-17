package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_routines",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["idRoutine"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["idPlant"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class PlantRoutineEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_routine") val idRoutine: Int,
    @ColumnInfo(name = "id_plant") val idPlant: Int
)