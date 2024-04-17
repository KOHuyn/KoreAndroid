package com.mobile.ads.admob.interstitial.listener

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.result.AdmobInterstitialResult
import com.mobile.ads.core.interstitial.listener.MobileInterstitialAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
class AdmobInterstitialAdListenerCollection :
    MobileAdListenerTransform<MobileInterstitialAdListener, Pair<InterstitialAdLoadCallback, FullScreenContentCallback>>() {
    private val interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            invokeAdListener {
                it.onAdLoaded(AdmobInterstitialResult(p0, this@AdmobInterstitialAdListenerCollection))
            }
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

    override fun invokeTransformListener(): Pair<InterstitialAdLoadCallback, FullScreenContentCallback> {
        return interstitialAdLoadCallback to fullScreenContentCallback
    }
}