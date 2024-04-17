package com.mobile.ads.admob.appopen.listener

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.result.AdmobAppOpenResult
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 07/03/2024.
 */
class AdmobAppOpenAdListenerCollection :
    MobileAdListenerTransform<MobileAppOpenAdListener, Pair<AppOpenAd.AppOpenAdLoadCallback, FullScreenContentCallback>>() {
    private val appOpenAdLoadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {

        override fun onAdLoaded(p0: AppOpenAd) {
            super.onAdLoaded(p0)
            invokeAdListener { it.onAdLoaded(AdmobAppOpenResult(p0, this@AdmobAppOpenAdListenerCollection)) }
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            invokeAdListener { it.onAdFailToLoad(AdmobAdError(p0)) }
        }
    }
    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
            invokeAdListener { it.onAdClick() }
        }

        override fun onAdImpression() {
            super.onAdImpression()
            invokeAdListener { it.onAdImpression() }
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            invokeAdListener { it.onAdClose() }
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            invokeAdListener {
                it.onAdFailToShow()
                it.onAdClose()
            }
        }
    }

    override fun invokeTransformListener(): Pair<AppOpenAd.AppOpenAdLoadCallback, FullScreenContentCallback> {
        return appOpenAdLoadCallback to fullScreenContentCallback
    }

}