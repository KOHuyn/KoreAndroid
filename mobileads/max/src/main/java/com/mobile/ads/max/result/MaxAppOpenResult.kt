package com.mobile.ads.max.result

import com.applovin.mediation.ads.MaxAppOpenAd
import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.max.appopen.listener.MaxAppOpenAdListenerCollection

data class MaxAppOpenResult(
    val appOpenAd: MaxAppOpenAd,
    override val listenerManager: MaxAppOpenAdListenerCollection
) : AppOpenResult(listenerManager)