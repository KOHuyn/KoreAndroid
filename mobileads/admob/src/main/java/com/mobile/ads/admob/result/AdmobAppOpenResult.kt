package com.mobile.ads.admob.result

import com.google.android.gms.ads.appopen.AppOpenAd
import com.mobile.ads.admob.appopen.listener.AdmobAppOpenAdListenerCollection
import com.mobile.ads.core.appopen.result.AppOpenResult

data class AdmobAppOpenResult(
    val appOpenAd: AppOpenAd,
    override val listenerManager: AdmobAppOpenAdListenerCollection
) : AppOpenResult(listenerManager)
