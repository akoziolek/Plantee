package com.example.plantee.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String? = null,
    val species: String? = null,
    val place: String? = null,
    val state: String? = null,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)