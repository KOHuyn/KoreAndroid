package com.mobile.ads.core.appopen.listener

import com.mobile.ads.core.appopen.result.AppOpenResult
import com.mobile.ads.listener.MobileAdListener

/**
 * Created by KO Huyn on 07/03/2024.
 */
abstract class MobileAppOpenAdListener : MobileAdListener() {
    open fun onAdLoaded(appOpenResult: AppOpenResult) = Unit
    open fun onAdClose() = Unit
    open fun onAdFailToShow() = Unit
}