package com.mobile.ads.max.nativead.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.listener.MobileAdListenerTransform
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.result.MaxNativeResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
class MaxNativeAdListenerCollection :
    MobileAdListenerTransform<MobileNativeAdListener, MaxNativeAdListener>() {
    override fun invokeTransformListener(): MaxNativeAdListener {
        return object : MaxNativeAdListener() {
            override fun onNativeAdClicked(p0: MaxAd) {
                super.onNativeAdClicked(p0)
                invokeAdListener { it.onAdClick() }
            }

            override fun onNativeAdLoaded(adView: MaxNativeAdView?, ad: MaxAd) {
                super.onNativeAdLoaded(adView, ad)
                invokeAdListener { it.onAdLoaded(MaxNativeResult(adView, ad, this@MaxNativeAdListenerCollection)) }
            }

            override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                super.onNativeAdLoadFailed(p0, p1)
                invokeAdListener { it.onAdFailToLoad(MaxAdError(p1)) }
            }
        }
    }
}