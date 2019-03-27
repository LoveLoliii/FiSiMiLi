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
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    private var context: Context? = null
    private var TAG = "FApplication"
    private val stack = LinkedList<Activity>()


    override fun onCreate() {
        super.onCreate()
        //LitePal.initialize(this)
        context = applicationContext
        init()
    }
    private fun init() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was Created " + " activity==null   "
                            + (activity == null) + "     activity.isFinishing()  " + activity.isFinishing + "    activity.isDestroyed()  " + activity.isDestroyed
                )
                if (!stack.contains(activity)) {
                    stack.add(activity)
                }
            }

            override fun onActivityStarted(activity: Activity?) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was Started " + " activity==null   "
                            + (activity == null) + "     activity.isFinishing()   " + activity.isFinishing + "   activity.isDestroyed()  " + activity.isDestroyed
                )
            }

            override fun onActivityResumed(activity: Activity?) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was oResumed" +  "activity==null   "
                            + (activity == null) + " activity.isFinishing()   " + activity.isFinishing + " activity.isDestroyed() " + activity.isDestroyed
                )
            }

            override fun onActivityPaused(activity: Activity?) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was Pauseed" + "activity==null   "
                            + (activity == null) + " activity.isFinishing()   " + activity.isFinishing + " activity.isDestroyed()  " + activity.isDestroyed
                )
            }

            override fun onActivityStopped(activity: Activity?) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was Stoped" + " activity==null    "
                            + (activity == null) + " activity.isFinishing()   " + activity.isFinishing + " activity.isDestroyed() " + activity.isDestroyed
                )
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was SaveInstanceState" + " activity==null "
                            + (activity == null) + " activity.isFinishing()   " + activity.isFinishing + " activity.isDestroyed()  " + activity.isDestroyed
                )
            }

            override fun onActivityDestroyed(activity: Activity?) {
                Log.e(
                    "Lifecycle", activity!!.localClassName + " was Destroyed" + " activity==null"
                            + (activity == null) + "  activity.isFinishing()  " + activity.isFinishing + "  activity.isDestroyed()" + activity.isDestroyed
                )
                if (stack.contains(activity)) {
                    stack.remove(activity)
                }
            }
        })
    }

    fun getTopActivity(): Activity? {
        var activity: Activity? = null
        if (!stack.isEmpty()) {
            activity = stack.last
        }
        return activity
    }

    fun getContext(): Context? {
        if (null == context) {
            Log.e(TAG, "getContext: PApplication work error")
        }
        return this.context
    }
    fun exit() {
        var activity: Activity
        while (!stack.isEmpty()) {
            activity = stack.pop()
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        System.exit(0)
        //Process.killProcess(Process.myPid())
    }
}