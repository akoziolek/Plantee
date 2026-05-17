package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.dto.DiagnosisSummaryDto
import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.relations.DiagnosisWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity): Long

    @Update
    suspend fun update(diagnosis: DiagnosisEntity)

    @Query("DELETE FROM diagnosis WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Transaction
    @Query("SELECT * FROM diagnosis WHERE id = :id")
    fun getDiagnosisWithDetails(id: Long): Flow<DiagnosisWithDetails?>

    @Query("""
        SELECT d.id, d.problem_description, d.diagnosed_at ,m.id AS media_id, m.file_path AS media_file_path
        FROM diagnosis d
        LEFT JOIN media m ON m.id = d.id_media
        WHERE d.id = :id
    """)
    fun getDiagnosisSummaryDto(id: Long): Flow<DiagnosisSummaryDto>
}