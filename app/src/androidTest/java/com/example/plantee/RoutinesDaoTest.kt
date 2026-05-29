package com.example.plantee

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.plantee.data.local.AppDatabase
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.entities.RoutineEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import app.cash.turbine.test
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class RoutinesDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var routinesDao: RoutinesDao

    private val today = LocalDate.of(2026, 5, 29)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        routinesDao = database.routinesDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun searchRoutines_shouldFilterByQuery_ignoringCaseAndMatchingPartially() = runTest {
        val routine1 = RoutineEntity(id = 1, name = "Watering flowers", activeDays = 127)
        val routine2 = RoutineEntity(id = 2, name = "Trimming hedge", activeDays = 127)
        val routine3 = RoutineEntity(id = 3, name = "watering succulents", activeDays = 127)

        routinesDao.insert(routine1)
        routinesDao.insert(routine2)
        routinesDao.insert(routine3)
        
        routinesDao.searchRoutines(
            searchQuery = "watering",
            filterActive = 0,
            today = today,
            selectedDays = 127
        ).test {
            val result = awaitItem()
            assertEquals(2, result.size)
            val names = result.map { it.name }
            assert(names.contains("Watering flowers"))
            assert(names.contains("watering succulents"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchRoutinesAsc_shouldReturnRoutinesInAlphabeticalOrder() = runTest {
        val routine1 = RoutineEntity(id = 1, name = "C", activeDays = 127)
        val routine2 = RoutineEntity(id = 2, name = "A", activeDays = 127)
        val routine3 = RoutineEntity(id = 3, name = "B", activeDays = 127)

        routinesDao.insert(routine1)
        routinesDao.insert(routine2)
        routinesDao.insert(routine3)

        routinesDao.searchRoutinesAsc("", 0, today, 127).test {
            val result = awaitItem()
            assertEquals(3, result.size)
            assertEquals("A", result[0].name)
            assertEquals("B", result[1].name)
            assertEquals("C", result[2].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchRoutinesDesc_shouldReturnRoutinesInReverseAlphabeticalOrder() = runTest {
        val routine1 = RoutineEntity(id = 1, name = "C", activeDays = 127)
        val routine2 = RoutineEntity(id = 2, name = "A", activeDays = 127)
        val routine3 = RoutineEntity(id = 3, name = "B", activeDays = 127)

        routinesDao.insert(routine1)
        routinesDao.insert(routine2)
        routinesDao.insert(routine3)

        routinesDao.searchRoutinesDesc("", 0, today, 127).test {
            val result = awaitItem()
            assertEquals(3, result.size)
            assertEquals("C", result[0].name)
            assertEquals("B", result[1].name)
            assertEquals("A", result[2].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchRoutines_withActiveFilter_shouldIgnoreDate_whenFilterActiveIsZero() = runTest {
        val futureRoutine = RoutineEntity(
            id = 1,
            name = "Test",
            startDate = today.plusDays(5),
            activeDays = 1
        )
        routinesDao.insert(futureRoutine)

        routinesDao.searchRoutines(
            searchQuery = "",
            filterActive = 0,
            today = today,
            selectedDays = 1
        ).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchRoutines_withActiveFilter_shouldFilterOut_whenBitmaskDoesNotMatch() = runTest {
        val routine = RoutineEntity(id = 1, name = "Friday routine", activeDays = 5)
        routinesDao.insert(routine)

        routinesDao.searchRoutines(
            searchQuery = "",
            filterActive = 1,
            today = today,
            selectedDays = 2
        ).test {
            val result = awaitItem()
            assertEquals(0, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}