package com.example.plantee

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import app.cash.turbine.test
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.ui.viewmodels.routine.RoutinesViewModel
import com.example.plantee.utils.DayBitmaskHelper
import com.example.plantee.utils.RoutineStatus
import com.example.plantee.utils.SortOrder
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    val testDispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RoutinesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: IRoutinesRepository = mockk()
    private lateinit var viewModel: RoutinesViewModel

    private val mockSearchResults = listOf(RoutineSummary(id = 1, name = "Trimming"))
    private val mockTodayResults = listOf(RoutineSummary(id = 2, name = "Watering"))

    @Before
    fun setUp() {
        every {
            repository.getSearchedRoutinesWithSortAndFilterSummary(any(), any(), any())
        } returns flowOf(mockSearchResults)

        every { repository.getRoutinesForDay(any()) } returns flowOf(mockTodayResults)

        viewModel = RoutinesViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `initial state should emit initial values and then load data from repository`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assert(initialState.isLoading)
            assert(initialState.routines.isEmpty())

            val loadedState = awaitItem()
            assertFalse(loadedState.isLoading)
            assertEquals(mockSearchResults, loadedState.routines)
            assertEquals(mockTodayResults, loadedState.todayRoutines)
        }
    }

    @Test
    fun `onSearchQueryChange should update state only after 300ms debounce`() = runTest {
        viewModel.state.test {
            awaitItem()
            awaitItem()

            viewModel.onSearchQueryChange("tri")

            advanceTimeBy(200)
            expectNoEvents()

            advanceTimeBy(150)

            verify { val flow = repository.getSearchedRoutinesWithSortAndFilterSummary("tri", any(), any()) }
        }
    }

    @Test
    fun `toggleFilterDay should modify bitmask when more than one day is selected`() = runTest {
        viewModel.filterState.test {
            val initialFilter = awaitItem()

            val initialMask = initialFilter.selectedDays

            viewModel.toggleFilterDay(DayOfWeek.MONDAY)

            val updatedFilter = awaitItem()

            assertTrue(updatedFilter.selectedDays != initialFilter.selectedDays)
            assertTrue(updatedFilter.selectedDays == 126)
        }
    }

    @Test
    fun `toggleFilterDay should NOT unselect day if it is the last selected day`() = runTest {
        viewModel.filterState.test {
            val initial = awaitItem()

            viewModel.toggleFilterDay(DayOfWeek.MONDAY)
            awaitItem()
            viewModel.toggleFilterDay(DayOfWeek.TUESDAY)
            awaitItem()
            viewModel.toggleFilterDay(DayOfWeek.WEDNESDAY)
            awaitItem()
            viewModel.toggleFilterDay(DayOfWeek.THURSDAY)
            awaitItem()
            viewModel.toggleFilterDay(DayOfWeek.FRIDAY)
            awaitItem()
            viewModel.toggleFilterDay(DayOfWeek.SATURDAY)

            val lastDayState = awaitItem()
            val maskWithOnlySunday = lastDayState.selectedDays

            assertEquals(1, DayBitmaskHelper.selectedDaysCount(maskWithOnlySunday))

            viewModel.toggleFilterDay(DayOfWeek.SUNDAY)

            expectNoEvents()

            assertEquals(maskWithOnlySunday, viewModel.filterState.value.selectedDays)
        }
    }

    @Test
    fun `toggleSortOrder should cycle through sort orders and trigger repository`() = runTest {
        viewModel.sortOrder.test {
            assertEquals(SortOrder.NONE, awaitItem())

            viewModel.toggleSortOrder()

            assertEquals(SortOrder.ASCENDING, awaitItem())

            viewModel.toggleSortOrder()

            assertEquals(SortOrder.DESCENDING, awaitItem())
        }
    }

    @Test
    fun `selectAllDays should change bitmask to max value`() = runTest {
        viewModel.filterState.test {
            val initialFilter = awaitItem()

            viewModel.toggleFilterDay(DayOfWeek.MONDAY)

            awaitItem()

            viewModel.selectAllDays()

            val updatedFilter = awaitItem()

            assertTrue(updatedFilter.selectedDays == 127)
        }
    }

    @Test
    fun `updateFilterStatus should change status in FilterState`() = runTest {
        viewModel.filterState.test {
            assertEquals(awaitItem().status, RoutineStatus.All)

            viewModel.updateFilterStatus(RoutineStatus.Active)

            assertEquals(awaitItem().status, RoutineStatus.Active)

            viewModel.updateFilterStatus(RoutineStatus.All)

            assertEquals(awaitItem().status, RoutineStatus.All)
        }
    }
}

