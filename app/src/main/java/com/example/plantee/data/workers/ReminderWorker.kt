package com.example.plantee.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.plantee.MainActivity
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
            val todayRoutines = routinesRepository.getRoutinesForDay(today).first()
            
            val pendingRoutines = todayRoutines.filter { it.lastlyDoneAt != today }

            if (pendingRoutines.isNotEmpty()) {
                sendNotification(tag)
            }
        }

        val hour = if (tag == NotificationScheduler.TAG_MORNING) {
            NotificationScheduler.MORNING_HOUR
        } else {
            NotificationScheduler.EVENING_HOUR
        }

        NotificationScheduler.scheduleDailyReminder(applicationContext, hour, 0, tag)

        Result.success()
    }

    private fun sendNotification(tag: String) {
        val channelId = "daily_reminders"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(applicationContext.getString(R.string.main_notification_title))
            .setContentText(applicationContext.getString(R.string.main_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(tag.hashCode(), notification)
    }
}
