package com.mobile.ads.max.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerTransform
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MaxViewMobileAdListenerTransform<I : MobileAdListener> :
    MobileAdListenerTransform<I, MaxAdViewAdListener>() {
    private val listMaxCallback =
        CopyOnWriteArrayList<MaxAdViewAdListener>().apply { add(invokeListener()) }

    fun addMaxListener(listener: MaxAdViewAdListener) {
        listMaxCallback.add(listener)
    }

    protected abstract fun invokeListener(): MaxAdViewAdListener

    override fun invokeTransformListener(): MaxAdViewAdListener {
        return object : MaxAdViewAdListener {
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

            override fun onAdExpanded(p0: MaxAd) {
                invokeMaxListener { it.onAdExpanded(p0) }
            }

            override fun onAdCollapsed(p0: MaxAd) {
                invokeMaxListener { it.onAdCollapsed(p0) }
            }
        }
    }

    fun removeMaxListener(listener: MaxAdViewAdListener) {
        listMaxCallback.remove(listener)
    }

    private fun invokeMaxListener(action: (adListener: MaxAdViewAdListener) -> Unit) {
        listMaxCallback.forEach(action)
    }
}