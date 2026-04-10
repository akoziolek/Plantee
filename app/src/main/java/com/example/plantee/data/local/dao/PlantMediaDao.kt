package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.plantee.data.local.entities.Plant
import com.example.plantee.data.local.entities.PlantsMedia

@Dao
interface PlantMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plantsMedia: PlantsMedia)

    @Update
    suspend fun update(plantsMedia: PlantsMedia)

    @Delete
    suspend fun delete(plantsMedia: PlantsMedia)
}