package com.mobile.ads.admob.banner.listener

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.listener.AdmobMobileAdListenerTransform
import com.mobile.ads.core.banner.listener.MobileBannerAdListener

/**
 * Created by KO Huyn on 05/03/2024.
 */
class AdmobBannerAdListenerCollection : AdmobMobileAdListenerTransform<MobileBannerAdListener>() {
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