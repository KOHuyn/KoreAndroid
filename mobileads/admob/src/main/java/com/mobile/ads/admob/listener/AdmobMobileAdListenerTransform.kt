package com.mobile.ads.admob.listener

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerTransform
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class AdmobMobileAdListenerTransform<I : MobileAdListener> :
    MobileAdListenerTransform<I, AdListener>() {
    private val listAdmobCallback =
        CopyOnWriteArrayList<AdListener>().apply { add(invokeListener()) }
    fun addAdmobListener(listener: AdListener) {
        listAdmobCallback.add(listener)
    }

    protected abstract fun invokeListener(): AdListener

    override fun invokeTransformListener(): AdListener {
        return object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                invokeAdmobListener { it.onAdClicked() }
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                invokeAdmobListener { it.onAdLoaded() }
            }

            override fun onAdClosed() {
                super.onAdClosed()
                invokeAdmobListener { it.onAdClosed() }
            }

            override fun onAdImpression() {
                super.onAdImpression()
                invokeAdmobListener { it.onAdImpression() }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                invokeAdmobListener { it.onAdFailedToLoad(p0) }
            }

            override fun onAdOpened() {
                super.onAdOpened()
                invokeAdmobListener { it.onAdOpened() }
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
                invokeAdmobListener { it.onAdSwipeGestureClicked() }
            }
        }
    }

    fun removeAdmobListener(listener: AdListener) {
        listAdmobCallback.remove(listener)
    }

    private fun invokeAdmobListener(action: (adListener: AdListener) -> Unit) {
        listAdmobCallback.forEach(action)
    }
}