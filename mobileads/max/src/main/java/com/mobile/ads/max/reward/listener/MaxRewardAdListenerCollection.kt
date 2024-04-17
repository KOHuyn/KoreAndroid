package com.mobile.ads.max.reward.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.mobile.ads.core.reward.listener.MobileRewardAdListener
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.listener.MaxRewardMobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
class MaxRewardAdListenerCollection :
    MaxRewardMobileAdListenerTransform<MobileRewardAdListener>() {
    override fun invokeListener(): MaxRewardedAdListener {
        return object : MaxRewardedAdListener {
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
                invokeAdListener { it.onAdFailToLoad(MaxAdError(p1)) }
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                invokeAdListener {
                    it.onAdFailToShow()
                    it.onAdClose()
                }
            }

            override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {
                invokeAdListener { it.onCollectReward() }
            }

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoStarted(p0: MaxAd) {

            }

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoCompleted(p0: MaxAd) {

            }
        }
    }

}