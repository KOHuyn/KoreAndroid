package com.mobile.ads.max.result

import com.applovin.mediation.ads.MaxInterstitialAd
import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.max.interstitial.listener.MaxInterstitialAdListenerCollection

data class MaxInterstitialResult(
    val interstitial: MaxInterstitialAd,
    override val listenerManager: MaxInterstitialAdListenerCollection
) : InterstitialResult(listenerManager)
