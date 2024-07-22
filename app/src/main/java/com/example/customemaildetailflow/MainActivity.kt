package com.example.customemaildetailflow

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {


    private lateinit var appbar:AppBarLayout
    private lateinit var toolbar:MaterialToolbar
    val fragmentEmailList = EmailListFragment()
    val fragmentEmailDetail = EmailDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appbar = findViewById(R.id.appBarLayout)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply {
            menu.setGroupVisible(R.id.group2,false)
            menu.setGroupVisible(R.id.group1,false)
        }

        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
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
        findViewById<MaterialToolbar?>(R.id.toolbar)?.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
            appbar.findViewById<MaterialToolbar>(R.id.toolbar)?.apply {
                title = "Email"
                navigationIcon = ContextCompat.getDrawable(baseContext,R.drawable.baseline_menu_24)
                toolbar.menu.setGroupVisible(R.id.group1,false)
                toolbar.menu.setGroupVisible(R.id.group2,false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appbar.findViewById<MaterialToolbar>(R.id.toolbar)?.apply {
            title = "Email"
            navigationIcon = ContextCompat.getDrawable(baseContext,R.drawable.baseline_menu_24)
            toolbar.menu.setGroupVisible(R.id.group1,false)
            toolbar.menu.setGroupVisible(R.id.group2,false)
        }
    }
}

