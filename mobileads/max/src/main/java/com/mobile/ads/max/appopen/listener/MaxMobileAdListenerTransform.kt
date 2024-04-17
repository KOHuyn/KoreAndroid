package com.mobile.ads.max.appopen.listener

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerTransform
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MaxMobileAdListenerTransform<I : MobileAdListener> :
    MobileAdListenerTransform<I, MaxAdListener>() {
    private val listMaxCallback =
        CopyOnWriteArrayList<MaxAdListener>().apply { add(invokeListener()) }
    private var maxListenerLoadAd: MaxAdListener? = null
    private var maxListenerShowAd: MaxAdListener? = null
    fun addMaxListener(listener: MaxAdListener) {
        listMaxCallback.add(listener)
    }

    fun setMaxListenerLoadAd(listener: MaxAdListener?) {
        this.maxListenerLoadAd = listener
    }

    fun setMaxListenerShowAd(listener: MaxAdListener?) {
        this.maxListenerShowAd = listener
    }

    protected abstract fun invokeListener(): MaxAdListener

    override fun invokeTransformListener(): MaxAdListener {
        return object : MaxAdListener {
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
        }
    }

    fun removeMaxListener(listener: MaxAdListener) {
        listMaxCallback.remove(listener)
    }

    private fun invokeMaxListener(action: (adListener: MaxAdListener) -> Unit) {
        listMaxCallback.forEach(action)
        maxListenerLoadAd?.let(action)
        maxListenerShowAd?.let(action)
    }
}