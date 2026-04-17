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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plantRoutine: PlantRoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plantRoutines: List<PlantRoutineEntity>)

    @Update
    suspend fun update(plantRoutine: PlantRoutineEntity)

    @Transaction
    suspend fun clearAndInsertNewForRoutine(routineId: Int, newPlantIds: List<Int>) {
        deleteByRoutineId(routineId)

        val newPlantRoutines = newPlantIds.map { id ->
            PlantRoutineEntity(idRoutine = routineId, idPlant = id)
        }

        insertAll(newPlantRoutines)
    }

    @Delete
    suspend fun delete(plantRoutine: PlantRoutineEntity)

    @Query("DELETE FROM plant_routines WHERE id_routine = :routineId")
    fun deleteByRoutineId(routineId: Int)

    @Query("SELECT * FROM plant_routines WHERE id_routine = :routineId ORDER BY id DESC")
    fun getPlantsRoutinesForRoutine(routineId: Int): Flow<List<PlantRoutineEntity>>

    @Query("SELECT * FROM plant_routines WHERE id_plant = :plantId ORDER BY id DESC")
    fun getPlantsRoutinesForPlant(plantId: Int): Flow<List<PlantRoutineEntity>>
}