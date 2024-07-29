package com.example.customemaildetailflow

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class AppWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {

    private val notificationManager1 = NotificationManagerCompat.from(applicationContext)
    override fun doWork(): Result {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            showProgressNotification(0)
        }

        if(inputData.getString("work").equals("sendEmail")){
            Thread.sleep(2000)
            println("Work Finished")
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(applicationContext,"Message Sent Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        else if(inputData.getString("work").equals("downloadData")){
            for(i in 1..20000){
                showProgressNotification(i)
            }
            notificationManager1.cancel(Thread.currentThread().id.toInt())
            showFinishedNotification()
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                notificationManager1.cancel(Thread.currentThread().id.toInt())
                Toast.makeText(applicationContext,"Data Downloaded Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        return Result.success()

    }
    override fun onStopped() {
        println("Worker Stopped")
        super.onStopped()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

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

    private fun showProgressNotification(progress:Int){
        val notification = NotificationCompat.Builder(applicationContext,"EmailNotificationID")
            .setContentTitle("Data Downloading")
            .setContentText("${((progress.toFloat()/20000F)*100).toInt()}")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setProgress(20000,progress,false)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager1.notify(Thread.currentThread().id.toInt(),notification)
        }
    }

    private fun showFinishedNotification(){
        val intent = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent, PendingIntent.FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(applicationContext,"EmailNotificationID")
            .setContentTitle("Data Downloaded Successfully")
            .setContentText("Demo File Name")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager1.notify(Thread.currentThread().id.toInt(),notification)
        }
    }
}