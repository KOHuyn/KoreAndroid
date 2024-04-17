package com.mobile.ads.core.appopen

import android.app.Activity
import android.app.Application
import android.util.Log
import com.mobile.ads.background.BackgroundDetector
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.core.appopen.request.AppOpenRequest
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.visibility.MobileAdVisibility
import java.lang.ref.WeakReference
import java.util.Date
import java.util.concurrent.atomic.AtomicBoolean

abstract class MobileAppOpenAdsResumeCore : MobileAppOpenAdsResume() {
    private var appOpenAd: AppOpenResult? = null
    private var appOpenRequest: WeakReference<AppOpenRequest>? = null
    private var loadTime: Long = 0
    private lateinit var application: Application
    private var canSkipShowAdResume = AtomicBoolean(false)
    private val listActivityDeniedShowAd = mutableSetOf<Class<*>>()
    private val backgroundDetector = BackgroundDetector.getInstance()

    private val foregroundListener = BackgroundDetector.ForegroundListener { activity ->
        if (canSkipShowAdResume.get()) {
            canSkipShowAdResume.set(false)
        } else {
            showAdIfAvailable(activity)
        }
    }

    override fun install(application: Application) {
        this.application = application
        backgroundDetector.install(application)
        backgroundDetector.removeForegroundListener(foregroundListener)
        backgroundDetector.addOnForegroundListener(foregroundListener)
    }

    override fun requestAdIfAvailable(request: AppOpenRequest) {
        this.appOpenRequest = WeakReference(request)
        if (!MobileAdVisibility.isVisibleAd()) return
        if (appOpenAd != null && wasLoadTimeLessThanNHoursAgo()) return
        requestAd(request)
    }

    override fun showAdIfAvailable(activity: Activity) {
        if (!MobileAdVisibility.isVisibleAd()) return
        val appOpenAd = appOpenAd
        if (appOpenAd != null && wasLoadTimeLessThanNHoursAgo()) {
            if (!listActivityDeniedShowAd.contains(activity::class.java)) {
                populateAd(activity, appOpenAd)
            }
        } else {
            appOpenRequest?.get()?.let(::requestAd)
        }
    }

    override fun deniedActivityShowAd(vararg activity: Class<*>) {
        listActivityDeniedShowAd.addAll(activity)
    }

    override fun skipShowAdResumeOneTime() {
        canSkipShowAdResume.compareAndSet(false, true)
    }

    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4L
    }

    private fun requestAd(request: AppOpenRequest) {
        requestAd(request, object : AdResultListener<AppOpenResult> {
            override fun onCompleteListener(result: AppOpenResult) {
                result.listenerManager.addListener(localAppOpenListener)
                loadTime = Date().time
                appOpenAd = result
            }

            override fun onFailureListener(error: MobileAdError) {

            }
        })
    }

    private val localAppOpenListener = object : MobileAppOpenAdListener() {
        override fun onAdClose() {
            appOpenAd = null
            appOpenRequest?.get()?.let(::requestAd)
        }

        override fun onAdFailToShow() {}
        override fun onAdClick() {}
        override fun onAdImpression() {
        }

        override fun onAdFailToLoad(error: MobileAdError) {
            Log.d("localAppOpenListener", "onAdFailToLoad:${error}")
        }
    }
}