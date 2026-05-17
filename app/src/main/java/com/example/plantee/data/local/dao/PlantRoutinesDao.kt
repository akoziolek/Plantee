package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.plantee.data.local.entities.PlantRoutineEntity

@Dao
interface PlantRoutinesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(plantRoutines: List<PlantRoutineEntity>): List<Long>

    @Transaction
    suspend fun clearAndInsertNewForRoutine(routineId: Long, newPlantIds: List<Long>) {
        deleteByRoutineId(routineId)

        val newPlantRoutines = newPlantIds.map { id ->
            PlantRoutineEntity(idRoutine = routineId, idPlant = id)
        }

        insertAll(newPlantRoutines)
    }

    @Query("DELETE FROM plant_routines WHERE id_routine = :routineId")
    fun deleteByRoutineId(routineId: Long)

    @Query("SELECT * FROM plant_routines WHERE id_routine = :routineId ORDER BY id DESC")
    fun getPlantsRoutinesForRoutine(routineId: Long): Flow<List<PlantRoutineEntity>>

    @Query("SELECT * FROM plant_routines WHERE id_plant = :plantId ORDER BY id DESC")
    fun getPlantsRoutinesForPlant(plantId: Long): Flow<List<PlantRoutineEntity>>

    @Query("SELECT id_routine FROM plant_routines WHERE id_plant = :plantId")
    suspend fun getRoutineIdsForPlant(plantId: Long): List<Long>

    @Query("DELETE FROM plant_routines WHERE id_routine = :routineId AND id_plant = :plantId")
    suspend fun deleteAssociation(routineId: Long, plantId: Long)

    @Query("SELECT COUNT(*) FROM plant_routines WHERE id_routine = :routineId")
    suspend fun getPlantCountForRoutine(routineId: Long): Int
}