package com.example.plantee.data.notifications

import android.content.Context
import android.icu.util.Calendar
import android.icu.util.MeasureUnit
import android.icu.util.TimeUnit
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.plantee.data.workers.ReminderWorker

object NotificationScheduler {
    fun scheduleDailyReminder(context: Context, hour: Int, minute: Int, tag: String) {
        val workManager = WorkManager.getInstance(context)

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(currentDate)) {
                add(Calendar.HOUR_OF_DAY, 24)
            }
        }

        val delay = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .addTag(tag)
            .build()

        workManager.enqueueUniqueWork(
            tag,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )
    }
}