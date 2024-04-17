package com.mobile.ads.max.banner.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.listener.MaxViewMobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
class MaxBannerAdListenerCollection :
    MaxViewMobileAdListenerTransform<MobileBannerAdListener>() {
    override fun invokeListener(): MaxAdViewAdListener {
        return object : MaxAdViewAdListener {
            override fun onAdLoaded(max: MaxAd) {}

            override fun onAdDisplayed(max: MaxAd) {
                invokeAdListener { it.onAdImpression() }
            }

            override fun onAdHidden(max: MaxAd) {}

            override fun onAdClicked(max: MaxAd) {
                invokeAdListener { it.onAdClick() }
            }

            override fun onAdLoadFailed(max: String, p1: MaxError) {
                invokeAdListener { it.onAdFailToLoad(MaxAdError(p1)) }
            }

            override fun onAdDisplayFailed(max: MaxAd, p1: MaxError) {}
            override fun onAdExpanded(p0: MaxAd) {}

            override fun onAdCollapsed(p0: MaxAd) {}
        }
    }

}