package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plantee.data.local.entities.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: Plant)

    @Update
    suspend fun update(plant: Plant)

    @Delete
    suspend fun delete(plant: Plant)

    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllPlants(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchPlants(searchQuery: String): Flow<List<Plant>>
}