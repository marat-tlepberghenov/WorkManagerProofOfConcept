package com.demo.workmanagerproofofconcept

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@HiltWorker
class SendLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: LocationRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(DISPATCHER_IO) {
        setForeground(createForegroundInfo())
        while (true) {
            sendLocation(repository.getLocation())
            delay(DELAY)
        }
        Result.retry()
    }

    private fun sendLocation(location: Location) {
        Log.d("SendLocationWorker", "Send location: $location")
    }

    private fun createForegroundInfo(): ForegroundInfo {
        createChannel()
        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Content title")
            .setTicker("Ticker")
            .setContentText("Content text")
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_background, "Open", notifyPendingIntent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    private fun createChannel() {
        val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Channel name", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NAME = "SendLocationWorker"
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "channelId"
        private val DISPATCHER_IO: CoroutineDispatcher = Dispatchers.IO
        private const val DELAY: Long = 3_000L
    }
}