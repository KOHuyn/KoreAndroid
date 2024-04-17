package com.mobile.ads.admob.request

import android.app.Activity
import com.mobile.ads.core.banner.request.MobileBannerRequest

data class AdmobBannerRequest(
    override val activity: Activity,
    override val adUnitId: String,
) : MobileBannerRequest(activity, adUnitId)