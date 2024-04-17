package com.mobile.ads.admob.result

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mobile.ads.admob.interstitial.listener.AdmobInterstitialAdListenerCollection
import com.mobile.ads.core.interstitial.result.InterstitialResult

data class AdmobInterstitialResult(
    val interstitial: InterstitialAd,
    override val listenerManager: AdmobInterstitialAdListenerCollection
) : InterstitialResult(listenerManager)