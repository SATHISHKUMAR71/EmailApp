package com.example.customemaildetailflow

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class EmailApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this,Configuration.Builder().build())
    }
}