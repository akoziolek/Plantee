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

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        sendNotification()

        val tag = tags.firstOrNull { it.contains("reminder") } ?: "reminder"
        val hour = if (tag == "morning") 9 else 17

        NotificationScheduler.scheduleDailyReminder(applicationContext, hour, 0, tag)

        return Result.success()
    }

    private fun sendNotification() {
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
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
