package com.mobile.ads.admob.appopen

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.mobile.ads.admob.appopen.listener.AdmobAppOpenAdListenerCollection
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.request.AdmobAppOpenRequest
import com.mobile.ads.admob.result.AdmobAppOpenResult
import com.mobile.ads.core.appopen.MobileAppOpenAds
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.loading.ResumeLoadingDialog
import com.mobile.ads.visibility.MobileAdVisibility
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 12/03/2024.
 */
object AdmobAppOpenAd : MobileAppOpenAds<AdmobAppOpenRequest> {

    override fun requestAd(
        request: AdmobAppOpenRequest,
        listener: AdResultListener<AppOpenResult>
    ) {
        val listenerManager = AdmobAppOpenAdListenerCollection()
        if (!MobileAdVisibility.isVisibleAd()) {
            listenerManager.invokeAdListener {
                it.onAdFailToLoad(MobileAdError.AnotherAdError)
            }
            listener.onFailureListener(MobileAdError.AnotherAdError)
            return
        }
        AppOpenAd.load(
            request.context,
            request.adUnitId,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    super.onAdLoaded(p0)
                    listener.onCompleteListener(AdmobAppOpenResult(p0, listenerManager))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    listener.onFailureListener(AdmobAdError(p0))
                }
            })

    }

    override suspend fun getAd(
        request: AdmobAppOpenRequest
    ): AdResult<AppOpenResult> = suspendCancellableCoroutine { cott ->
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
        if (result is AdmobAppOpenResult) {
            val appOpenAd = result.appOpenAd
            appOpenAd.fullScreenContentCallback =
                result.listenerManager.invokeTransformListener().second
            val dialog = ResumeLoadingDialog(activity)
            kotlin.runCatching {
                dialog.show()
            }
            result.listenerManager.addListener(object : MobileAppOpenAdListener() {
                override fun onAdClose() {
                    super.onAdClose()
                    kotlin.runCatching { dialog.dismiss() }
                }

                override fun onAdFailToShow() {
                    super.onAdFailToShow()
                    kotlin.runCatching { dialog.dismiss() }
                }

            })
            Handler(Looper.getMainLooper()).postDelayed({
                appOpenAd.show(activity)
            }, 200)
        }
    }
}