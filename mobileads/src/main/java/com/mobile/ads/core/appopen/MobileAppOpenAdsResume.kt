package com.mobile.ads.core.appopen

import android.app.Activity
import android.app.Application
import com.mobile.ads.core.appopen.request.AppOpenRequest

abstract class MobileAppOpenAdsResume : MobileAppOpenAds<AppOpenRequest> {
    abstract fun requestAdIfAvailable(request: AppOpenRequest)
    abstract fun showAdIfAvailable(activity: Activity)
    abstract fun install(application: Application)
    abstract fun deniedActivityShowAd(vararg activity: Class<*>)
    abstract fun skipShowAdResumeOneTime()
}
