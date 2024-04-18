package com.mobile.ads.core.appopen

import android.app.Activity
import android.app.Application
import com.mobile.ads.core.appopen.request.AppOpenRequest

abstract class MobileAppOpenAdsResume<REQUEST : AppOpenRequest> : MobileAppOpenAds<REQUEST> {
    abstract fun requestAdIfAvailable(request: REQUEST)
    abstract fun showAdIfAvailable(activity: Activity)
    abstract fun install(application: Application)
    abstract fun deniedActivityShowAd(vararg activity: Class<*>)
    abstract fun skipShowAdResumeOneTime()
}
