package com.mobile.ads.admob.request

import android.app.Activity
import com.mobile.ads.core.interstitial.request.MobileInterstitialRequest

data class AdmobInterstitialRequest(
    override val activity: Activity,
    override val adUnitId: String,
) : MobileInterstitialRequest(activity, adUnitId)