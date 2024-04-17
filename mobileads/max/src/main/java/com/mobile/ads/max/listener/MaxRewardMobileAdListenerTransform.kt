package com.mobile.ads.max.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerTransform
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MaxRewardMobileAdListenerTransform<I : MobileAdListener> :
    MobileAdListenerTransform<I, MaxRewardedAdListener>() {
    private val listMaxCallback =
        CopyOnWriteArrayList<MaxRewardedAdListener>().apply { add(invokeListener()) }

    fun addMaxListener(listener: MaxRewardedAdListener) {
        listMaxCallback.add(listener)
    }

    protected abstract fun invokeListener(): MaxRewardedAdListener

    override fun invokeTransformListener(): MaxRewardedAdListener {
        return object : MaxRewardedAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                invokeMaxListener { it.onAdLoaded(p0) }
            }

            override fun onAdDisplayed(p0: MaxAd) {
                invokeMaxListener { it.onAdDisplayed(p0) }
            }

            override fun onAdHidden(p0: MaxAd) {
                invokeMaxListener { it.onAdHidden(p0) }
            }

            override fun onAdClicked(p0: MaxAd) {
                invokeMaxListener { it.onAdClicked(p0) }
            }

            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                invokeMaxListener { it.onAdLoadFailed(p0, p1) }
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                invokeMaxListener { it.onAdDisplayFailed(p0, p1) }
            }

            override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {
                invokeMaxListener { it.onUserRewarded(p0, p1) }
            }

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoStarted(p0: MaxAd) {
                invokeMaxListener { it.onRewardedVideoStarted(p0) }
            }

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoCompleted(p0: MaxAd) {
                invokeMaxListener { it.onRewardedVideoStarted(p0) }
            }
        }
    }

    fun removeMaxListener(listener: MaxRewardedAdListener) {
        listMaxCallback.remove(listener)
    }

    private fun invokeMaxListener(action: (adListener: MaxRewardedAdListener) -> Unit) {
        listMaxCallback.forEach(action)
    }
}