package com.mobile.ads.core.banner.listener

import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.listener.MobileAdListener

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileBannerAdListener : MobileAdListener() {
    open fun onAdLoaded(bannerResult: BannerResult) {}
}