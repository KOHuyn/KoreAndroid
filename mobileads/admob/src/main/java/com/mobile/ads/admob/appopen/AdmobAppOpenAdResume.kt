package com.mobile.ads.admob.appopen

import android.app.Activity
import com.mobile.ads.admob.request.AdmobAppOpenRequest
import com.mobile.ads.core.appopen.MobileAppOpenAdsResumeCore
import com.mobile.ads.core.appopen.request.AppOpenRequest
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener

/**
 * Created by KO Huyn on 18/04/2024.
 */
object AdmobAppOpenAdResume : MobileAppOpenAdsResumeCore<AdmobAppOpenRequest>() {
    override fun requestAd(
        request: AdmobAppOpenRequest,
        listener: AdResultListener<AppOpenResult>
    ) {
        AdmobAppOpenAd.requestAd(request, listener)
    }

    override fun populateAd(activity: Activity, result: AppOpenResult) {
        AdmobAppOpenAd.populateAd(activity, result)
    }

    override suspend fun getAd(request: AdmobAppOpenRequest): AdResult<AppOpenResult> {
        return AdmobAppOpenAd.getAd(request)
    }
}