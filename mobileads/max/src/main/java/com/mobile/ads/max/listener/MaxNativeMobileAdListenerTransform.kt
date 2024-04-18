package com.mobile.ads.max.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerTransform
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 18/04/2024.
 */
abstract class MaxNativeMobileAdListenerTransform<I : MobileAdListener> :
    MobileAdListenerTransform<I, MaxNativeAdListener>() {
    private val listMaxCallback =
        CopyOnWriteArrayList<MaxNativeAdListener>().apply { add(invokeListener()) }

    fun addMaxListener(listener: MaxNativeAdListener) {
        listMaxCallback.add(listener)
    }

    protected abstract fun invokeListener(): MaxNativeAdListener

    override fun invokeTransformListener(): MaxNativeAdListener {
        return object : MaxNativeAdListener() {
            override fun onNativeAdClicked(p0: MaxAd) {
                super.onNativeAdClicked(p0)
                invokeMaxListener { it.onNativeAdClicked(p0) }
            }

            override fun onNativeAdExpired(p0: MaxAd) {
                super.onNativeAdExpired(p0)
                invokeMaxListener { it.onNativeAdExpired(p0) }
            }

            override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                super.onNativeAdLoadFailed(p0, p1)
                invokeMaxListener { it.onNativeAdLoadFailed(p0, p1) }
            }

            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd) {
                super.onNativeAdLoaded(p0, p1)
                invokeMaxListener { it.onNativeAdLoaded(p0, p1) }
            }
        }
    }

    fun removeMaxListener(listener: MaxNativeAdListener) {
        listMaxCallback.remove(listener)
    }

    private fun invokeMaxListener(action: (adListener: MaxNativeAdListener) -> Unit) {
        listMaxCallback.forEach(action)
    }
}