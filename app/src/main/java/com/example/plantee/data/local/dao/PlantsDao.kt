package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.entities.FullPlantEntity
import com.example.plantee.data.local.entities.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: PlantEntity): Long

    @Update
    suspend fun update(plant: PlantEntity)

    @Delete
    suspend fun delete(plant: PlantEntity)

    @Query("DELETE FROM plants WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Transaction
    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllFullPlants(): Flow<List<FullPlantEntity>>

    @Transaction
    @Query("SELECT * FROM plants WHERE id IN (:ids) ORDER BY id DESC")
    fun getPlantsByIds(ids: List<Long>): Flow<List<FullPlantEntity>>

    @Query("SELECT * FROM plants WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchPlants(searchQuery: String): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlant(id: Long): Flow<PlantEntity?>

    @Transaction
    @Query("SELECT * FROM plants WHERE id = :id")
    fun getFullPlant(id: Long): Flow<FullPlantEntity?>
}