package com.example.plantee.data.local.dao

import android.R
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

    @Transaction
    @Query("""
    SELECT r.*, rs.id_diagnosis AS id_diagnosis
    FROM routines r
    LEFT JOIN plant_routines pr ON pr.id_routine = r.id
    LEFT JOIN routine_sources rs ON rs.id_plant_routine = pr.id
    WHERE (r.active_days & :dayMask) != 0
    AND (r.start_date IS NULL OR r.start_date <= :date)
    AND (r.end_date IS NULL OR r.end_date >= :date)
""")
    fun getRoutinesRequiredForDate(date: LocalDate, dayMask: Int): Flow<List<RoutineWithDetails>>

    @Query("""
        SELECT *
        FROM routines
        WHERE 
            (start_date IS NULL OR start_date <= :today)
            AND
            (end_date IS NULL OR :today <= end_date)
            AND
            (active_days & :dayBitmap) > 0
    """)
    fun getRoutinesForWeekday(dayBitmap: Int, today: LocalDate): Flow<List<RoutineEntity>>

    @Query("""SELECT * FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (start_date <= :today AND :today <= end_date))
            AND
            ((active_days & :selectedDays) != 0)
    """)
    fun searchRoutines(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineEntity>>

    @Query("""SELECT * FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (start_date <= :today AND :today <= end_date))
            AND
            ((active_days & :selectedDays) != 0)
        ORDER BY name ASC
    """)
    fun searchRoutinesAsc(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineEntity>>

    @Query("""SELECT * FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (start_date <= :today AND :today <= end_date))
            AND
            ((active_days & :selectedDays) != 0)
        ORDER BY name DESC
    """)
    fun searchRoutinesDesc(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineEntity>>

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

    @Query("""
    SELECT * FROM routines 
    WHERE (start_date IS NULL OR start_date <= :endDate) 
    AND (end_date IS NULL OR end_date >= :startDate)
""")
    suspend fun getRoutinesActiveInPeriod(startDate: LocalDate, endDate: LocalDate): List<RoutineEntity>
}