package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plantee.data.local.entities.DiagnosisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity)

    @Update
    suspend fun update(diagnosis: DiagnosisEntity)

    @Delete
    suspend fun delete(diagnosis: DiagnosisEntity)

    @Query("SELECT * FROM diagnosis WHERE id_plant = :plantId ORDER BY id DESC")
    fun getDiagnosesForPlant(plantId: Int): Flow<List<DiagnosisEntity>>
}