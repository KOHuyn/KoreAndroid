package com.mobile.ads.max.result

import com.applovin.mediation.ads.MaxAdView
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.max.banner.listener.MaxBannerAdListenerCollection

data class MaxBannerResult(
    val adView: MaxAdView,
    val listenerManager: MaxBannerAdListenerCollection
) : BannerResult(adView, listenerManager)
