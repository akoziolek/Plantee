package com.example.plantee

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.plantee.data.notifications.NotificationScheduler
import com.example.plantee.data.workers.ReminderWorker
import com.example.plantee.di.RepositoryModule
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IDiagnosesRepository
import com.example.plantee.domain.repositories.IMediaRepository
import com.example.plantee.domain.repositories.IPhotosRepository
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.repositories.IRoutinesStatisticsRepository
import com.example.plantee.domain.repositories.ISettingsRepository
import com.example.plantee.domain.repositories.IUserPreferencesRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class NotificationTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    private lateinit var context: Context

    @BindValue
    @JvmField
    val routinesRepository = mockk<IRoutinesRepository>(relaxed = true)

    @BindValue
    @JvmField
    val settingsRepository = mockk<ISettingsRepository>(relaxed = true)

    @BindValue
    @JvmField
    val plantsRepository = mockk<IPlantsRepository>(relaxed = true)

    @BindValue
    @JvmField
    val diagnosesRepository = mockk<IDiagnosesRepository>(relaxed = true)

    @BindValue
    @JvmField
    val photosRepository = mockk<IPhotosRepository>(relaxed = true)

    @BindValue
    @JvmField
    val userPreferencesRepository = mockk<IUserPreferencesRepository>(relaxed = true)

    @BindValue
    @JvmField
    val routineStatisticsRepository = mockk<IRoutinesStatisticsRepository>(relaxed = true)

    @BindValue
    @JvmField
    val mediaRepository = mockk<IMediaRepository>(relaxed = true)

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    @Test
    fun testWorker_WithUnfinishedRoutines_SendsNotification() = runBlocking {
        coEvery { settingsRepository.getNotificationsEnabled() } returns flowOf(true)
        val today = LocalDate.now()
        val routines = listOf(
            RoutineSummary(id = 1, name = "Water", lastlyDoneAt = today.minusDays(1))
        )
        coEvery { routinesRepository.getRoutinesForDay(today) } returns flowOf(routines)

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context)
            .setTags(listOf(NotificationScheduler.TAG_MORNING))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notificationFound: Boolean = withTimeoutOrNull(5000) {
            while (true) {
                val notifications = notificationManager.activeNotifications
                if (notifications.any { it.id == NotificationScheduler.TAG_MORNING.hashCode() }) {
                    break
                }
                delay(100)
            }
            true
        } ?: false

        assertTrue("Notification should be visible in the system", notificationFound)
    }

    @Test
    fun testWorker_WithAllRoutinesFinished_DoesNotSendNotification() = runBlocking {
        coEvery { settingsRepository.getNotificationsEnabled() } returns flowOf(true)
        val today = LocalDate.now()
        val routines = listOf(
            RoutineSummary(id = 1, name = "Water", lastlyDoneAt = today)
        )
        coEvery { routinesRepository.getRoutinesForDay(today) } returns flowOf(routines)

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context)
            .setTags(listOf(NotificationScheduler.TAG_MORNING))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications
        assertTrue("No notification should be visible",
            notifications.none { it.id == NotificationScheduler.TAG_MORNING.hashCode() })
    }

    @Test
    fun testWorker_WithUnfinishedRoutinesButDisabled_DoesNotSendNotification() = runBlocking {
        coEvery { settingsRepository.getNotificationsEnabled() } returns flowOf(false)
        val today = LocalDate.now()
        val routines = listOf(
            RoutineSummary(id = 1, name = "Water", lastlyDoneAt = today.minusDays(1))
        )
        coEvery { routinesRepository.getRoutinesForDay(today) } returns flowOf(routines)

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context)
            .setTags(listOf(NotificationScheduler.TAG_MORNING))
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications
        assertTrue("No notification should be visible when disabled",
            notifications.none { it.id == NotificationScheduler.TAG_MORNING.hashCode() })
    }

    @Test
    fun testWorker_ReschedulesItself() = runBlocking {
        coEvery { settingsRepository.getNotificationsEnabled() } returns flowOf(true)
        coEvery { routinesRepository.getRoutinesForDay(any()) } returns flowOf(emptyList())

        val worker = TestListenableWorkerBuilder<ReminderWorker>(context)
            .setTags(listOf(NotificationScheduler.TAG_MORNING))
            .build()

        worker.doWork()

        val workManager = androidx.work.WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosByTag(NotificationScheduler.TAG_MORNING).get()
        assertTrue("A new work request should be enqueued for rescheduling", workInfos.isNotEmpty())
    }
}
