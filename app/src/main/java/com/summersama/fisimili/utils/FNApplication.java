package com.summersama.fisimili.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;


public class FNApplication extends Application {
    private static Context context;
    static String TAG = "FApplication";
    private static LinkedList<Activity> stack = new LinkedList<>();
    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        context = getApplicationContext();
        init();
    }

    private void init() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was Created");
                if (!stack.contains(activity)) {
                    stack.add(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was Started"  );
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was oResumed"  );
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was Pauseed"  );
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was Stoped"  );
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("Lifecycle", activity.getLocalClassName() + " was SaveInstanceState"  );
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("Lifecycle",activity.getLocalClassName()+" was Destroyed" );
                if (stack.contains(activity)) {
                    stack.remove(activity);
                }
            }
        });
    }
    public static Activity topActivity () {
        Activity activity = null;
        if (!stack.isEmpty()) {
            activity = stack.getLast();
        }
        return activity;
    }
    public static Context getContext(){
        if(null==context){
            Log.e(TAG, "getContext: PApplication work error" );
        }
        return context;
    }

    public void exit() {
        Activity activity;
        while (!stack.isEmpty()) {
            activity = stack.pop();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
        Process.killProcess(Process.myPid());
    }
}
