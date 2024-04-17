package com.mobile.ads.admob.interstitial

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mobile.ads.core.interstitial.MobileInterstitialAds
import com.mobile.ads.core.interstitial.listener.MobileInterstitialAdListener
import com.mobile.ads.core.interstitial.request.MobileInterstitialRequest
import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.admob.interstitial.listener.AdmobInterstitialAdListenerCollection
import com.mobile.ads.admob.request.AdmobInterstitialRequest
import com.mobile.ads.admob.result.AdmobInterstitialResult
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object AdmobInterstitialAds : MobileInterstitialAds<AdmobInterstitialRequest> {
    override fun requestAd(
        request: AdmobInterstitialRequest,
        listener: AdResultListener<InterstitialResult>
    ) {
        val listenerManager = AdmobInterstitialAdListenerCollection()
        val adRequest = AdRequest.Builder().build()
        listenerManager.addListener(object : MobileInterstitialAdListener() {
            override fun onAdLoaded(interstitialResult: InterstitialResult) {
                super.onAdLoaded(interstitialResult)
                listener.onCompleteListener(interstitialResult)
            }

            override fun onAdFailToLoad(error: MobileAdError) {
                super.onAdFailToLoad(error)
                listener.onFailureListener(error)
            }
        })
        InterstitialAd.load(
            request.activity,
            request.adUnitId,
            adRequest,
            listenerManager.invokeTransformListener().first
        )
    }

    override suspend fun getAd(request: AdmobInterstitialRequest): AdResult<InterstitialResult> =
        suspendCancellableCoroutine { cott ->
            requestAd(request, object : AdResultListener<InterstitialResult> {
                override fun onCompleteListener(result: InterstitialResult) {
                    cott.safeResume(AdResult.Success(result))
                }

                override fun onFailureListener(error: MobileAdError) {
                    cott.safeResume(AdResult.Failure(error))
                }
            })
        }


    override fun populateAd(activity: Activity, result: InterstitialResult) {
        if (result is AdmobInterstitialResult) {
            val interstitialAds = result.interstitial
            interstitialAds.fullScreenContentCallback =
                result.listenerManager.invokeTransformListener().second
            interstitialAds.show(activity)
        }
    }
}