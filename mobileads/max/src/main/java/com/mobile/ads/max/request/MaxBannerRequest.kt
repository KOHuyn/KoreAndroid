package com.mobile.ads.max.request

import android.app.Activity
import com.mobile.ads.core.banner.request.MobileBannerRequest

data class MaxBannerRequest(
    override val activity: Activity,
    override val adUnitId: String,
) : MobileBannerRequest(activity, adUnitId)