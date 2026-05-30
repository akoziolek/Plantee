package com.example.plantee

import com.example.plantee.data.local.dao.RoutinesDao
import com.example.plantee.data.local.dao.RoutinesStatisticsDao
import com.example.plantee.data.local.dto.RoutineSummaryDto
import com.example.plantee.data.local.entities.RoutinesStatisticsEntity
import com.example.plantee.data.repositories.RoutinesStatisticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate


class RoutineStatisticsRepositoryUnitTest {
    private val routinesStatisticsDao: RoutinesStatisticsDao = mockk(relaxUnitFun = true)
    private val routinesDao: RoutinesDao = mockk()
    private lateinit var routinesStatisticsRepository: RoutinesStatisticsRepository

    private val today = LocalDate.of(2026, 5, 30)
    private val yesterday = today.minusDays(1)

    @Before
    fun setUp() {
        routinesStatisticsRepository = RoutinesStatisticsRepository(routinesStatisticsDao, routinesDao)
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns today
    }

    @After
    fun tearDown() {
        unmockkStatic(LocalDate::class)
    }


    @Test
    fun `syncStreak should initialize with 0 streak when stats do not exist`() = runTest {
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns null

        routinesStatisticsRepository.syncStreak()

        coVerify(exactly = 1) {
            routinesStatisticsDao.upsertRoutineStatistic(
                RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 0)
            )
        }
    }

    @Test
    fun `syncStreak should do nothing when last update was yesterday`() = runTest {
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 1)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity

        routinesStatisticsRepository.syncStreak()
        coVerify(exactly = 0) { routinesStatisticsDao.upsertRoutineStatistic(any()) }
    }

    @Test
    fun `syncStreak should do nothing when last update was today`() = runTest {
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = today, currentStreak = 1)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity

        routinesStatisticsRepository.syncStreak()
        coVerify(exactly = 0) { routinesStatisticsDao.upsertRoutineStatistic(any()) }
    }

    @Test
    fun `syncStreak should increment streak when gap exists and routines were completed`() = runTest {
        val twoDaysAgo = today.minusDays(2)
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = twoDaysAgo, currentStreak = 12)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity
        val mockRoutine = RoutineSummaryDto(
            id = 1L,
            name = "mock routine",
            description = "",
            lastlyDoneAt = yesterday,
        )
        coEvery { routinesDao.getRoutinesForWeekday(any(), yesterday) } returns flowOf(listOf(mockRoutine))

        routinesStatisticsRepository.syncStreak()

        coVerify(exactly = 1) {
            routinesStatisticsDao.upsertRoutineStatistic(
                RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 13)
            )
        }
    }

    @Test
    fun `syncStreak should reset streak to 0 when gap exists and routines were not completed`() = runTest {
        val twoDaysAgo = yesterday.minusDays(1)
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = twoDaysAgo, currentStreak = 5)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity

        val mockRoutine = RoutineSummaryDto(
            id = 1L,
            name = "mock routine",
            description = "",
            lastlyDoneAt = twoDaysAgo,
        )
        coEvery { routinesDao.getRoutinesForWeekday(any(), yesterday) } returns flowOf(listOf(mockRoutine))

        routinesStatisticsRepository.syncStreak()

        coVerify(exactly = 1) {
            routinesStatisticsDao.upsertRoutineStatistic(
                RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 0)
            )
        }
    }

    @Test
    fun `getEffectiveStreak should increment displayed streak if all routines for today are done`() = runTest {
        val mockStatFlow = flowOf(RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 10))
        coEvery { routinesStatisticsDao.getRoutinesStatisticsFlow() } returns mockStatFlow

        val mockTodayRoutine = RoutineSummaryDto(
            id = 1L,
            name = "mock routine",
            description = "",
            lastlyDoneAt = today,
        )
        coEvery { routinesDao.getRoutinesForWeekday(any(), today) } returns flowOf(listOf(mockTodayRoutine))

        val resultStreak = routinesStatisticsRepository.getEffectiveStreak(today).first()

        assertEquals(11, resultStreak)
    }

    @Test
    fun `getEffectiveStreak should return base streak if today routines are not fulfilled`() = runTest {
        val mockStatFlow = flowOf(RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 10))
        coEvery { routinesStatisticsDao.getRoutinesStatisticsFlow() } returns mockStatFlow

        val mockTodayRoutine = RoutineSummaryDto(
            id = 1L,
            name = "mock routine",
            description = "",
            lastlyDoneAt = yesterday,
        )
        coEvery { routinesDao.getRoutinesForWeekday(any(), today) } returns flowOf(listOf(mockTodayRoutine))

        val resultStreak = routinesStatisticsRepository.getEffectiveStreak(today).first()

        assertEquals(10, resultStreak)
    }

    @Test
    fun `getEffectiveStreak should maintain base streak when today has no scheduled routines`() = runTest {
        val mockStatFlow = flowOf(RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 10))
        coEvery { routinesStatisticsDao.getRoutinesStatisticsFlow() } returns mockStatFlow
        coEvery { routinesDao.getRoutinesForWeekday(any(), today) } returns flowOf(emptyList())

        val resultStreak = routinesStatisticsRepository.getEffectiveStreak(today).first()

        assertEquals(10, resultStreak)
    }

    @Test
    fun `syncStreak should maintain streak when gap exists but no routines were scheduled`() = runTest {
        val fourDaysAgo = yesterday.minusDays(3)
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = fourDaysAgo, currentStreak = 5)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity

        coEvery { routinesDao.getRoutinesForWeekday(any(), any()) } returns flowOf(emptyList())

        routinesStatisticsRepository.syncStreak()

        coVerify(exactly = 1) {
            routinesStatisticsDao.upsertRoutineStatistic(
                RoutinesStatisticsEntity(lastStreakUpdate = yesterday, currentStreak = 5)
            )
        }
    }

    @Test
    fun `syncStreak should abort and do nothing when user manipulates time backwards`() = runTest {
        val mockEntity = RoutinesStatisticsEntity(lastStreakUpdate = today, currentStreak = 10)
        coEvery { routinesStatisticsDao.getRoutinesStatistics() } returns mockEntity

        val manipulatedToday = today.minusDays(2)
        every { LocalDate.now() } returns manipulatedToday

        routinesStatisticsRepository.syncStreak()

        coVerify(exactly = 0) { routinesStatisticsDao.upsertRoutineStatistic(any()) }
    }
}