package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.entities.PlantRoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantRoutinesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plantRoutine: PlantRoutineEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(plantRoutines: List<PlantRoutineEntity>): List<Long>

    @Update
    suspend fun update(plantRoutine: PlantRoutineEntity)

    @Transaction
    suspend fun clearAndInsertNewForRoutine(routineId: Long, newPlantIds: List<Long>) {
        deleteByRoutineId(routineId)

        val newPlantRoutines = newPlantIds.map { id ->
            PlantRoutineEntity(idRoutine = routineId, idPlant = id)
        }

        insertAll(newPlantRoutines)
    }

    @Delete
    suspend fun delete(plantRoutine: PlantRoutineEntity)

    @Query("DELETE FROM plant_routines WHERE id_routine = :routineId")
    fun deleteByRoutineId(routineId: Long)

    @Query("SELECT * FROM plant_routines WHERE id_routine = :routineId ORDER BY id DESC")
    fun getPlantsRoutinesForRoutine(routineId: Long): Flow<List<PlantRoutineEntity>>

    @Query("SELECT * FROM plant_routines WHERE id_plant = :plantId ORDER BY id DESC")
    fun getPlantsRoutinesForPlant(plantId: Long): Flow<List<PlantRoutineEntity>>
}