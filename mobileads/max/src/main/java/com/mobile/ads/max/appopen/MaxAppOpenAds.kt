package com.mobile.ads.max.appopen

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.mobile.ads.max.appopen.listener.MaxAppOpenAdListenerCollection
import com.mobile.ads.core.appopen.MobileAppOpenAds
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.core.appopen.request.AppOpenRequest
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.loading.ResumeLoadingDialog
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.request.MaxAppOpenRequest
import com.mobile.ads.max.result.MaxAppOpenResult
import com.mobile.ads.visibility.MobileAdVisibility
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 12/03/2024.
 */
object MaxAppOpenAds : MobileAppOpenAds<MaxAppOpenRequest> {

    override fun requestAd(
        request: MaxAppOpenRequest,
        listener: AdResultListener<AppOpenResult>
    ) {
        val listenerManager = MaxAppOpenAdListenerCollection()
        if (!MobileAdVisibility.isVisibleAd()) {
            listenerManager.invokeAdListener { it.onAdFailToLoad(MobileAdError.AnotherAdError) }
            listener.onFailureListener(MobileAdError.AnotherAdError)
            return
        }
        val appOpenAd = MaxAppOpenAd(request.adUnitId, request.context)
        listenerManager.setMaxListenerLoadAd(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                listener.onCompleteListener(MaxAppOpenResult(appOpenAd, listenerManager))
            }

            override fun onAdDisplayed(p0: MaxAd) {}
            override fun onAdHidden(p0: MaxAd) {}
            override fun onAdClicked(p0: MaxAd) {}
            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                listener.onFailureListener(MaxAdError(p1))
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {}
        })
        appOpenAd.setListener(listenerManager.invokeTransformListener())
        appOpenAd.loadAd()
    }

    override suspend fun getAd(
        request: MaxAppOpenRequest
    ): AdResult<AppOpenResult> =
        suspendCancellableCoroutine { cott ->
            requestAd(request, object : AdResultListener<AppOpenResult> {
                override fun onCompleteListener(result: AppOpenResult) {
                    cott.safeResume(AdResult.Success(result))
                }

                override fun onFailureListener(error: MobileAdError) {
                    cott.safeResume(AdResult.Failure(error))
                }
            })
        }

    override fun populateAd(activity: Activity, result: AppOpenResult) {
        if (!MobileAdVisibility.isVisibleAd()) {
            result.listenerManager.invokeAdListener {
                it.onAdFailToShow()
            }
            return
        }
        if (result is MaxAppOpenResult) {
            val appOpenAd = result.appOpenAd
            if (appOpenAd.isReady) {
                val dialog = ResumeLoadingDialog(activity)
                kotlin.runCatching {
                    dialog.show()
                }
                result.listenerManager.addListener(object : MobileAppOpenAdListener() {
                    override fun onAdClose() {
                        super.onAdClose()
                        dialog.dismiss()
                    }

                    override fun onAdFailToShow() {
                        super.onAdFailToShow()
                        dialog.dismiss()
                    }
                })
                Handler(Looper.getMainLooper()).postDelayed({
                    appOpenAd.showAd()
                }, 800)
            }
        }
    }
}