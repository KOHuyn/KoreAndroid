package com.mobile.ads.max.request

import android.app.Activity
import com.mobile.ads.core.interstitial.request.MobileInterstitialRequest

data class MaxInterstitialRequest(
    override val activity: Activity,
    override val adUnitId: String,
) : MobileInterstitialRequest(activity, adUnitId)
