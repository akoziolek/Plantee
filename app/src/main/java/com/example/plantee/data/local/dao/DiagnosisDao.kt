package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.Diagnosis

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: Diagnosis)

    @Update
    suspend fun update(diagnosis: Diagnosis)

    @Delete
    suspend fun delete(diagnosis: Diagnosis)
}