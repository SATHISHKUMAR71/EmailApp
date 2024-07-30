package com.example.customemaildetailflow
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
class PeriodicSyncWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val intent = Intent(applicationContext,EmailSyncForegroundService::class.java)
        applicationContext.startForegroundService(intent)
        return Result.success()
    }
}