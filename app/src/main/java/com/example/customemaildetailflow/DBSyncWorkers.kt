package com.example.customemaildetailflow

import android.app.IntentService
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Job


class DBSyncWorkers(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {
        Thread.sleep(5000)
        println("Work Finished")
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(applicationContext,"Message Sent Successfully",Toast.LENGTH_SHORT).show()
        }
        return Result.success()
    }
    override fun onStopped() {
        println("Worker Stopped")
        super.onStopped()
    }
}