package com.mobile.ads.helper

import com.mobile.ads.core.banner.MobileBannerAds
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.core.banner.result.BannerResult

class BannerAdHelper<REQUEST : MobileBannerRequest>(strategy: MobileBannerAds<REQUEST>) :
    AdWithViewHelper<REQUEST, BannerResult, MobileBannerAdListener, MobileBannerAds<REQUEST>>(strategy) {
    override fun configResultWithListener(result: BannerResult) {
        result.listener.addListener(*listListener.toTypedArray())
    }
}
