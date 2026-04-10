package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plantee.data.local.entities.PlantRoutine
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantRoutinesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plantRoutine: PlantRoutine)

    @Update
    suspend fun update(plantRoutine: PlantRoutine)

    @Delete
    suspend fun delete(plantRoutine: PlantRoutine)

    @Query("SELECT * FROM plant_routines WHERE id_routine = :routineId ORDER BY id DESC")
    fun getPlantsRoutinesForRoutine(routineId: Int): Flow<List<PlantRoutine>>
}