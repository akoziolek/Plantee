package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.relations.RoutineWithDetails
import com.example.plantee.data.local.entities.RoutineEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RoutinesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity): Long

    @Update
    suspend fun update(routine: RoutineEntity)

    @Query("UPDATE routines SET lastly_done_at = :date WHERE id = :id")
    suspend fun updateLastlyDoneAt(id: Long, date: LocalDate? = LocalDate.now())

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM routines ORDER BY id DESC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Transaction
    @Query("""
        SELECT r.*, rs.id_diagnosis AS id_diagnosis
        FROM routines r
        LEFT JOIN plant_routines pr ON pr.id_routine = r.id
        LEFT JOIN routine_sources rs ON rs.id_plant_routine = pr.id
    """)
    fun getAllRoutinesWithDetails(): Flow<List<RoutineWithDetails>>

    @Transaction
    @Query("""
        SELECT r.*, rs.id_diagnosis AS id_diagnosis
        FROM routines r
        LEFT JOIN plant_routines pr ON pr.id_routine = r.id
        LEFT JOIN routine_sources rs ON rs.id_plant_routine = pr.id
        WHERE lastly_done_at = :date
    """)
    fun getRoutinesWithDate(date: LocalDate): Flow<List<RoutineWithDetails>>

    @Query("""
        SELECT *
        FROM routines
        WHERE (active_days & :dayBitmap) > 0
    """)
    fun getRoutinesForWeekday(dayBitmap: Int): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchRoutines(searchQuery: String): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchRoutinesAsc(searchQuery: String): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name DESC")
    fun searchRoutinesDesc(searchQuery: String): Flow<List<RoutineEntity>>

    // TODO WHERE (value & :mask) <> 0

    @Transaction
    @Query("""
        SELECT r.*, rs.id_diagnosis AS id_diagnosis
        FROM routines r
        LEFT JOIN plant_routines pr ON pr.id_routine = r.id
        LEFT JOIN routine_sources rs ON rs.id_plant_routine = pr.id
        WHERE r.id = :id
    """)
    fun getRoutineWithDetails(id: Long): Flow<RoutineWithDetails?>

    @Query("SELECT * FROM routines WHERE id = :id")
    fun getRoutine(id: Long): Flow<RoutineEntity?>
}