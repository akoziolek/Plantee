package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.entities.FullDiagnosisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity): Int

    @Update
    suspend fun update(diagnosis: DiagnosisEntity)

    @Delete
    suspend fun delete(diagnosis: DiagnosisEntity)

    @Query("DELETE FROM diagnosis WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM diagnosis WHERE id_plant = :plantId ORDER BY id DESC")
    fun getDiagnosesForPlant(plantId: Int): Flow<List<DiagnosisEntity>>

    @Transaction
    @Query("SELECT * FROM diagnosis WHERE id_plant = :plantId ORDER BY id DESC")
    fun getDiagnosesWithDetailsForPlant(plantId: Int): Flow<List<FullDiagnosisEntity>>

    @Transaction
    @Query("SELECT * FROM diagnosis WHERE id = :id")
    fun getDiagnosisWithDetails(id: Int): Flow<FullDiagnosisEntity?>
}