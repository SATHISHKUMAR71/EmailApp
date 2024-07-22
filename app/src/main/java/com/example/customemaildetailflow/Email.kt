package com.example.customemaildetailflow

import androidx.lifecycle.MutableLiveData


data class Email(var title:String,var subtitle:String,var date:String,var content:String,var isStarred:Boolean,var isViewed:Boolean,var important:String,var notifySender:Boolean)