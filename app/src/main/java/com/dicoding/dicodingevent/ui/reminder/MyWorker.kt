package com.dicoding.dicodingevent.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.ui.EventModelFactory
import com.dicoding.dicodingevent.utils.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val factory = EventModelFactory.getInstance(applicationContext)
            val viewModel = factory.create(ReminderViewModel::class.java)
            val reminderEvent = viewModel.setNotification()

            if (reminderEvent != null) {
                reminderEvent.name?.let { name ->
                    reminderEvent.date?.let { date ->
                        val dateFormatted = DateFormatter.convertDateFormat(date)
                        Log.d(TAG, "doWork: Showing notification for event: $name on $dateFormatted")
                        withContext(Dispatchers.Main) {
                            showNotification(name, dateFormatted)
                        }
                    }
                }
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }


    private fun showNotification(title: String, description: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private const val TAG = "MyWorker"
        private const val CHANNEL_ID = "daily_remainder_channel"
        private const val CHANNEL_NAME = "Daily Event Reminders"
        const val NOTIFICATION_ID = 1
    }
}