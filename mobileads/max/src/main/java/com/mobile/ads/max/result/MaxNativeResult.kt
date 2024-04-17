package com.mobile.ads.max.result

import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.max.nativead.listener.MaxNativeAdListenerCollection

data class MaxNativeResult(
    val maxNativeAdView: MaxNativeAdView?,
    val maxAd: MaxAd,
    override val listenerManager: MaxNativeAdListenerCollection
) : NativeResult(listenerManager)
