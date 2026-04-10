package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plantee.data.local.entities.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutinesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine)

    @Update
    suspend fun update(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

    @Query("SELECT * FROM routines ORDER BY id DESC")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routines WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchRoutines(searchQuery: String): Flow<List<Routine>>
}