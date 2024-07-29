package com.example.customemaildetailflow

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters


class AppWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {

        if(inputData.getString("work").equals("sendEmail")){
            Thread.sleep(2000)
            println("Work Finished")
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(applicationContext,"Message Sent Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        else if(inputData.getString("work").equals("downloadData")){
            Thread.sleep(20000)
            println("Data Downloaded Successfully ${Thread.currentThread().id}")
            println("Data Downloaded Successfully ${Thread.currentThread().name}")
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(applicationContext,"Data Downloaded Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        return Result.success()

    }
    override fun onStopped() {
        println("Worker Stopped")
        super.onStopped()
    }
}