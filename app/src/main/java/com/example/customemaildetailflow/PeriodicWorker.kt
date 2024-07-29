package com.example.customemaildetailflow
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
class PeriodicWorker(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {
        createNotificationChannel()
        startWorkInBg()
        return Result.success()

    }
    private fun startWorkInBg() {
        val intent = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent, PendingIntent.FLAG_MUTABLE)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationManager1 = NotificationManagerCompat.from(applicationContext)
            val notification = NotificationCompat.Builder(applicationContext,"EmailNotificationID")
                .setContentTitle("Checking for New Email")
                .setContentText("New Email")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager1.notify(1,notification)
            }
        }
        println("Notification Pushed From Email App")

    }
    override fun onStopped() {
        super.onStopped()
        println("Worker 2 On Stop")
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "EmailNotificationID"
            val channelName = "NormalNotification"
            val channelDescription = "Pushes the Email Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}