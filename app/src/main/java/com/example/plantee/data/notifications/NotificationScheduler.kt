package com.example.plantee.data.notifications

import android.content.Context
import android.icu.util.Calendar
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.plantee.data.workers.ReminderWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    const val MORNING_HOUR = 8
    const val EVENING_HOUR = 20

    const val TAG_MORNING = "morning_reminder"
    const val TAG_EVENING = "evening_reminder"

    fun scheduleDailyReminder(context: Context, hour: Int, minute: Int, tag: String) {
        val workManager = WorkManager.getInstance(context)

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            while (timeInMillis <= currentDate.timeInMillis + 10_000) {
                // 1-minute interval for debugging purposes
                // TODO change to add(Calendar.DAY_OF_YEAR, 1)
                add(Calendar.MINUTE, 1)
            }
        }

        val delay = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(tag)
            .build()

        workManager.enqueueUniqueWork(
            tag,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )
    }
}
