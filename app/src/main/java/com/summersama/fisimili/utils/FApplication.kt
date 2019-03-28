package com.summersama.fisimili.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.util.Log
import java.util.*



class FApplication : Application() {
    override fun onCreate() {
        super.onCreate()
     //   LitePal.initialize(this)
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

}