package com.mobile.ads.core.banner

import android.view.ViewGroup
import com.mobile.ads.core.IMobileAd
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.core.banner.result.BannerResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileBannerAds<REQUEST : MobileBannerRequest> : IMobileAd<REQUEST, BannerResult> {
    fun populateAd(viewGroup: ViewGroup, result: BannerResult)
}