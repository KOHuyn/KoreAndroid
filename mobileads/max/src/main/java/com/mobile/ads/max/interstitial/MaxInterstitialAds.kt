package com.mobile.ads.max.interstitial

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.mobile.ads.core.interstitial.MobileInterstitialAds
import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.interstitial.listener.MaxInterstitialAdListenerCollection
import com.mobile.ads.max.request.MaxInterstitialRequest
import com.mobile.ads.max.result.MaxInterstitialResult
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object MaxInterstitialAds : MobileInterstitialAds<MaxInterstitialRequest> {

    override fun requestAd(
        request: MaxInterstitialRequest,
        listener: AdResultListener<InterstitialResult>
    ) {
        val interstitial = MaxInterstitialAd(request.adUnitId, request.activity)
        val listenerManager = MaxInterstitialAdListenerCollection()
        listenerManager.addMaxListener(object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                val interstitialResult = MaxInterstitialResult(interstitial, listenerManager)
                listenerManager.invokeAdListener {
                    it.onAdLoaded(interstitialResult)
                }
                listener.onCompleteListener(interstitialResult)
            }

            override fun onAdDisplayed(p0: MaxAd) {}
            override fun onAdHidden(p0: MaxAd) {}
            override fun onAdClicked(p0: MaxAd) {}
            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                listener.onFailureListener(MaxAdError(p1))
            }
            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {}
        })
        interstitial.setListener(listenerManager.invokeTransformListener())
        interstitial.loadAd()
    }

    override suspend fun getAd(request: MaxInterstitialRequest): AdResult<InterstitialResult> =
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
        if (result is MaxInterstitialResult) {
            val interstitialAd = result.interstitial
            if (interstitialAd.isReady) {
                interstitialAd.showAd()
            }
        }
    }
}