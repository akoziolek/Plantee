package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.dto.RoutineSummaryDto
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.relations.RoutineWithDetails
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

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT id, name, description, lastly_done_at
        FROM routines
        WHERE 
            (start_date IS NULL OR start_date <= :date)
            AND
            (end_date IS NULL OR :date <= end_date)
            AND
            (active_days & :dayMask) > 0
    """)
    fun getRoutinesForWeekday(dayMask: Int, date: LocalDate): Flow<List<RoutineSummaryDto>>

    @Query("""SELECT id, name, description, lastly_done_at FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (
                (start_date IS NULL OR start_date <= :today) 
                AND (end_date IS NULL OR :today <= end_date))
            )
            AND
            ((active_days & :selectedDays) != 0)
    """)
    fun searchRoutines(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineSummaryDto>>

    @Query("""SELECT id, name, description, lastly_done_at FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (
                (start_date IS NULL OR start_date <= :today) 
                AND (end_date IS NULL OR :today <= end_date)))
            AND
            ((active_days & :selectedDays) != 0)
        ORDER BY name ASC
    """)
    fun searchRoutinesAsc(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineSummaryDto>>

    @Query("""SELECT id, name, description, lastly_done_at FROM routines 
        WHERE 
            name LIKE '%' || :searchQuery || '%'
            AND
            (:filterActive = 0 OR (
                (start_date IS NULL OR start_date <= :today) 
                AND (end_date IS NULL OR :today <= end_date))
            )
            AND
            ((active_days & :selectedDays) != 0)
        ORDER BY name DESC
    """)
    fun searchRoutinesDesc(searchQuery: String, filterActive: Int, today: LocalDate, selectedDays: Int): Flow<List<RoutineSummaryDto>>

    @Transaction
    @Query("""
        SELECT r.*
        FROM routines r
        LEFT JOIN plant_routines pr ON pr.id_routine = r.id
        WHERE r.id = :id
    """)
    fun getRoutineWithDetails(id: Long): Flow<RoutineWithDetails?>

    @Query("""
    SELECT * FROM routines 
    WHERE (start_date IS NULL OR start_date <= :endDate) 
    AND (end_date IS NULL OR end_date >= :startDate)
""")
    suspend fun getRoutinesActiveInPeriod(startDate: LocalDate, endDate: LocalDate): List<RoutineEntity>
}