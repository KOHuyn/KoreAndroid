package com.mobile.ads.listener

import com.mobile.ads.error.MobileAdError

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileAdListener {
    open fun onAdClick() = Unit
    open fun onAdImpression() = Unit
    open fun onAdFailToLoad(error: MobileAdError) = Unit

}