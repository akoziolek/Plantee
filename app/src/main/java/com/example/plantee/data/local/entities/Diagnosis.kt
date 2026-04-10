package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "diagnosis",
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["idPlant"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Diagnosis(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String? = null,
    @ColumnInfo(name = "sun_level") val sunLevel: Int,
    @ColumnInfo(name = "moisture_level") val moistureLevel: Int,
    @ColumnInfo(name = "diagnosed_at") val diagnosedAt: LocalDateTime,
    @ColumnInfo(name = "id_plant") val idPlant: Int? = null
)
