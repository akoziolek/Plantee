package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.relations.DiagnosisWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity): Long

    @Transaction
    @Query("SELECT * FROM diagnosis WHERE id = :id")
    fun getDiagnosisWithDetails(id: Long): Flow<DiagnosisWithDetails?>
}