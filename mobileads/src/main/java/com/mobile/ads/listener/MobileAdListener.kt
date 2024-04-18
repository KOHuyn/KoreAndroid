package com.mobile.ads.listener

import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.error.MobileAdError

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileAdListener {
    open fun onAdClick() {
        if (this !is MobileAppOpenAdListener) {
            MobileAdListenerGlobal.invokeListener { it.setOnClickAdListener() }
        }
    }

    open fun onAdImpression() {}

    open fun onAdFailToLoad(error: MobileAdError) {}
}