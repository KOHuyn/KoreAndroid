package com.mobile.ads.admob.result

import com.google.android.gms.ads.nativead.NativeAd
import com.mobile.ads.admob.nativead.listener.AdmobNativeAdListenerCollection
import com.mobile.ads.core.nativead.result.NativeResult

data class AdmobNativeResult(
    val native: NativeAd,
    override val layoutNativeId: Int,
    override val listenerManager: AdmobNativeAdListenerCollection
) : NativeResult(listenerManager,layoutNativeId)
