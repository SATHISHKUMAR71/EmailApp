package com.example.customemaildetailflow

import android.Manifest
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import java.security.Permissions
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    private lateinit var appbar:AppBarLayout
    private lateinit var toolbar:MaterialToolbar
    val fragmentEmailList = EmailListFragment()
    val fragmentEmailDetail = EmailDetailFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),103)
        }
        val workManager = WorkManager.getInstance(application.applicationContext)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorker = PeriodicWorkRequest.Builder(PeriodicWorker::class.java,15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork("Data Sync",
            ExistingPeriodicWorkPolicy.KEEP,periodicWorker)
        if ((savedInstanceState == null)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentEmailList, fragmentEmailList, "Email Fragment List")
                .commit()
        }
        if (resources.configuration.screenWidthDp >= 700) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentEmailList, fragmentEmailList, "Email Fragment List")
                .replace(R.id.fragmentEmailDetail, fragmentEmailDetail)
                .commit()
        }
        if(resources.configuration.screenWidthDp<700){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentEmailList, fragmentEmailList, "Email Fragment List")
                .commit()
        }
    }

}

