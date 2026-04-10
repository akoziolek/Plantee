package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.DiagnosisMedia

@Dao
interface DiagnosisMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosisMedia: DiagnosisMedia)

    @Update
    suspend fun update(diagnosisMedia: DiagnosisMedia)

    @Delete
    suspend fun delete(diagnosisMedia: DiagnosisMedia)
}