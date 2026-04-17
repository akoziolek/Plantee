package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.DiagnosisMediaEntity
import com.example.plantee.data.local.entities.PlantRoutineEntity

@Dao
interface DiagnosisMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosisMedia: DiagnosisMediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(diagnosisMedia: List<DiagnosisMediaEntity>)

    @Update
    suspend fun update(diagnosisMedia: DiagnosisMediaEntity)

    @Delete
    suspend fun delete(diagnosisMedia: DiagnosisMediaEntity)
}