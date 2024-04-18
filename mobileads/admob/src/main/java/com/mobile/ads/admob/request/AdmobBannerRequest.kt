package com.mobile.ads.admob.request

import android.app.Activity
import com.mobile.ads.admob.banner.listener.AdmobBannerAdListenerCollection
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.listener.MobileAdListenerTransform

data class AdmobBannerRequest(
    override val activity: Activity,
    override val adUnitId: String,
) : MobileBannerRequest(activity, adUnitId)