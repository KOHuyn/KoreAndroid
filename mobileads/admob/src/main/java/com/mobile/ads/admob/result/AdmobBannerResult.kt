package com.mobile.ads.admob.result

import com.google.android.gms.ads.AdView
import com.mobile.ads.admob.banner.listener.AdmobBannerAdListenerCollection
import com.mobile.ads.core.banner.result.BannerResult

data class AdmobBannerResult(
    val adView: AdView,
    val listenerManager: AdmobBannerAdListenerCollection
) : BannerResult(adView, listenerManager)