package com.example.customemaildetailflow

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class EmailSyncForegroundService : Service() {

    private lateinit var notificationManager: NotificationManagerCompat
    companion object {
        private const val CHANNEL_ID = "EmailNotificationID"
        private const val NOTIFICATION_ID_PROGRESS = 1
        private const val NOTIFICATION_ID_FINISHED = 2
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        // Initialize NotificationManagerCompat
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("OnStartCommand")

        // Create and show the initial notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Email Sync Service")
            .setContentText("Data Sync in Progress")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID_PROGRESS, notification)

        // Start the service in a separate thread
        Thread {
            for (i in 1..20000) {
                println("OnStartCommand service started: $i")
                showProgressNotification(i)
            }
            showFinishedNotification()
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showProgressNotification(progress: Int) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Data Syncing")
            .setContentText("${((progress.toFloat() / 20000F) * 100).toInt()}%")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setProgress(20000, progress, false)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {return
        }
        notificationManager.notify(NOTIFICATION_ID_PROGRESS, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, "Email Sync Service Channel", importance).apply {
            description = "Notification channel for Email Sync Service"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showFinishedNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Synced Successfully")
            .setContentText("Data Synced Successfully")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {return
        }
        notificationManager.notify(NOTIFICATION_ID_FINISHED, notification)
    }
}
