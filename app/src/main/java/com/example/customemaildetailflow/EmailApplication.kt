package com.example.customemaildetailflow

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class EmailApplication:Application(),Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setExecutor(ThreadPoolExecutor(
                10,
                12,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue()
            ))
            .build()
}