package com.example.plantee.unitTests

import app.cash.turbine.test
import com.example.plantee.data.local.dao.PlantRoutinesDao
import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.dto.RoutineSummaryDto
import com.example.plantee.data.repositories.RoutinesRepository
import com.example.plantee.ui.viewmodels.routine.FilterState
import com.example.plantee.utils.RoutineStatus
import com.example.plantee.utils.SortOrder
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import java.time.LocalDate
import org.junit.Test
import org.junit.Assert.assertEquals


class RoutinesRepositoryTest {
    private val routinesDao: RoutinesDao = mockk()
    private val plantsRoutinesDao: PlantRoutinesDao = mockk()

    private lateinit var repository: RoutinesRepository

    private val sampleQuery = "Watering"
    private val sampleDays = 19
    private val today: LocalDate = LocalDate.now()

    private val dtoList = listOf(
        RoutineSummaryDto(id = 1, name = "Watering plants")
    )

    @Before
    fun setUp() {
        repository = RoutinesRepository(routinesDao, plantsRoutinesDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getSearchedRoutines when sort is NONE should call searchRoutines on DAO`() = runTest {
        val filter = FilterState(status = RoutineStatus.Active, selectedDays = sampleDays)

        every {
            routinesDao.searchRoutines(sampleQuery, 1, today, sampleDays)
        } returns flowOf(dtoList)

        repository.getSearchedRoutinesWithSortAndFilterSummary(sampleQuery, SortOrder.NONE, filter).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Watering plants", result[0].name)
            awaitComplete()
        }

        verify(exactly = 1) { val flow = routinesDao.searchRoutines(sampleQuery, 1, today, sampleDays) }
    }

    @Test
    fun `getSearchedRoutines when sort is ASCENDING should call searchRoutinesAsc on DAO`() = runTest {
        val filter = FilterState(status = RoutineStatus.All, selectedDays = sampleDays)

        every {
            routinesDao.searchRoutinesAsc(sampleQuery, 0, today, sampleDays)
        } returns flowOf(dtoList)

        repository.getSearchedRoutinesWithSortAndFilterSummary(sampleQuery, SortOrder.ASCENDING, filter).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Watering plants", result[0].name)
            awaitComplete()
        }

        verify(exactly = 1) { val flow = routinesDao.searchRoutinesAsc(sampleQuery, 0, today, sampleDays) }
    }

    @Test
    fun `getSearchedRoutines when sort is DESCENDING should call searchRoutinesDesc on DAO`() = runTest {
        val filter = FilterState(status = RoutineStatus.Active, selectedDays = sampleDays)

        every {
            routinesDao.searchRoutinesDesc(sampleQuery, 1, today, sampleDays)
        } returns flowOf(dtoList)

        repository.getSearchedRoutinesWithSortAndFilterSummary(sampleQuery, SortOrder.DESCENDING, filter).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Watering plants", result[0].name)
            awaitComplete()
        }

        verify(exactly = 1) { val flow = routinesDao.searchRoutinesDesc(sampleQuery, 1, today, sampleDays) }
    }

    @Test
    fun `getSearchedRoutines when status is Active should pass filterActive as 1 to DAO`() = runTest {
        val filter = FilterState(status = RoutineStatus.Active, selectedDays = sampleDays)

        every {
            routinesDao.searchRoutines(any(), 1, any(), any())
        } returns flowOf(dtoList)

        repository.getSearchedRoutinesWithSortAndFilterSummary("", SortOrder.NONE, filter).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Watering plants", result[0].name)
            awaitComplete()
        }

        verify { val flow = routinesDao.searchRoutines(any(), filterActive = 1, any(), any()) }
    }

    @Test
    fun `getSearchedRoutines when status is NOT Active should pass filterActive as 0 to DAO`() = runTest {
        val filter = FilterState(status = RoutineStatus.All, selectedDays = sampleDays)

        every {
            routinesDao.searchRoutines(any(), 0, any(), any())
        } returns flowOf(dtoList)

        repository.getSearchedRoutinesWithSortAndFilterSummary("", SortOrder.NONE, filter).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Watering plants", result[0].name)
            awaitComplete()
        }

        verify { val flow = routinesDao.searchRoutines(any(), filterActive = 0, any(), any()) }
    }
}