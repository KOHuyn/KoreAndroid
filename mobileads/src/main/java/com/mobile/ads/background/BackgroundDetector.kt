package com.mobile.ads.background

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by KO Huyn on 08/03/2024.
 */
class BackgroundDetector private constructor() {
    companion object {
        private const val TAG = "BackgroundDetector"

        @Volatile
        private var _instance: BackgroundDetector? = null

        @Synchronized
        fun getInstance(): BackgroundDetector = synchronized(this) {
            return _instance ?: BackgroundDetector().also { _instance = it }
        }
    }

    private var isInitialized: Boolean = false
    private val foregroundListenerManager = CopyOnWriteArrayList<ForegroundListener>()
    private val backgroundListenerManager = CopyOnWriteArrayList<BackgroundListener>()
    private val countActivitiesActive = AtomicInteger(0)
    private var isActivityStarted: Boolean = false
    val isFromBackground get() = countActivitiesActive.get() == 0
    var isForegroundPending: Boolean = true

    fun install(application: Application) {
        if (!isInitialized) {
            isInitialized = true
            application.unregisterActivityLifecycleCallbacks(activitiesCallbackListener)
            application.registerActivityLifecycleCallbacks(activitiesCallbackListener)
        }
    }

    fun addOnForegroundListener(listener: ForegroundListener) {
        foregroundListenerManager.add(listener)
    }

    fun removeForegroundListener(listener: ForegroundListener) {
        foregroundListenerManager.remove(listener)
    }

    fun addOnBackgroundListener(listener: BackgroundListener) {
        backgroundListenerManager.add(listener)
    }

    fun removeBackgroundListener(listener: BackgroundListener) {
        backgroundListenerManager.remove(listener)
    }

    private val activitiesCallbackListener = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            if (countActivitiesActive.incrementAndGet() == 1) {
                if (isActivityStarted) {
                    isForegroundPending = true
                } else {
                    isActivityStarted = true
                }
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (isForegroundPending) {
                foregroundListenerManager.forEach { it.setOnForegroundListener(activity) }
                Log.d(TAG, "onForeground:${activity::class.java.simpleName}")
                isForegroundPending = false
            }
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            if (countActivitiesActive.getAndDecrement() == 1) {
                backgroundListenerManager.forEach { it.setOnBackgroundListener() }
                Log.d(TAG, "onBackground:${activity::class.java.simpleName}")
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }

    fun interface ForegroundListener {
        fun setOnForegroundListener(currentActivity: Activity)
    }

    fun interface BackgroundListener {
        fun setOnBackgroundListener()
    }

}