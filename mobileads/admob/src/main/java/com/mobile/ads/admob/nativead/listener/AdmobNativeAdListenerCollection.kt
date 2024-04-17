package com.mobile.ads.admob.nativead.listener

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.listener.AdmobMobileAdListenerTransform
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener

/**
 * Created by KO Huyn on 05/03/2024.
 */
class AdmobNativeAdListenerCollection : AdmobMobileAdListenerTransform<MobileNativeAdListener>() {

    override fun invokeListener(): AdListener {
        return object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                invokeAdListener { it.onAdClick() }
            }

            override fun onAdImpression() {
                super.onAdImpression()
                invokeAdListener { it.onAdImpression() }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                invokeAdListener { it.onAdFailToLoad(AdmobAdError(p0)) }
            }
        }
    }
}