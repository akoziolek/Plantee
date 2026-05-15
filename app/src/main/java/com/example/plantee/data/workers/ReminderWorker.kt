package com.example.plantee.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.plantee.R
import com.example.plantee.data.notifications.NotificationScheduler
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.repositories.ISettingsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ReminderWorkerEntryPoint {
        fun routinesRepository(): IRoutinesRepository
        fun settingsRepository(): ISettingsRepository
    }

    override fun doWork(): Result = runBlocking {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            ReminderWorkerEntryPoint::class.java
        )
        val routinesRepository = entryPoint.routinesRepository()
        val settingsRepository = entryPoint.settingsRepository()

        val isEnabled = settingsRepository.getNotificationsEnabled().first()
        
        val tag = tags.firstOrNull { 
            it == NotificationScheduler.TAG_MORNING || it == NotificationScheduler.TAG_EVENING 
        } ?: NotificationScheduler.TAG_MORNING

        if (isEnabled) {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek.value
            val todayRoutines = routinesRepository.getRoutinesForWeekdaySummary(dayOfWeek).first()
            
            val pendingRoutines = todayRoutines.filter { it.lastlyDoneAt != today }

            if (pendingRoutines.isNotEmpty()) {
                sendNotification(tag)
            }
        }

        // Reschedule for the next day
        val hour = if (tag == NotificationScheduler.TAG_MORNING) {
            NotificationScheduler.MORNING_HOUR
        } else {
            NotificationScheduler.EVENING_HOUR
        }

        NotificationScheduler.scheduleDailyReminder(applicationContext, hour, 15, tag)

        Result.success()
    }

    private fun sendNotification(tag: String) {
        val channelId = "daily_reminders"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(applicationContext.getString(R.string.main_notification_title))
            .setContentText(applicationContext.getString(R.string.main_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(tag.hashCode(), notification)
    }
}
