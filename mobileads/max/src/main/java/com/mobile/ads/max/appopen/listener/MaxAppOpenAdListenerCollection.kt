package com.mobile.ads.max.appopen.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener

/**
 * Created by KO Huyn on 07/03/2024.
 */
class MaxAppOpenAdListenerCollection :
    MaxMobileAdListenerTransform<MobileAppOpenAdListener>() {
    override fun invokeListener(): MaxAdListener {
        return object : MaxAdListener {
            override fun onAdLoaded(p0: MaxAd) {

            }

            override fun onAdDisplayed(p0: MaxAd) {
                invokeAdListener { it.onAdImpression() }
            }

            override fun onAdHidden(p0: MaxAd) {
                invokeAdListener { it.onAdClose() }
            }

            override fun onAdClicked(p0: MaxAd) {
                invokeAdListener { it.onAdClick() }
            }

            override fun onAdLoadFailed(p0: String, p1: MaxError) {
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                invokeAdListener {
                    it.onAdFailToShow()
                    it.onAdClose()
                }
            }
        }
    }
}