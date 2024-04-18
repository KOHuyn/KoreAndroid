package com.mobile.ads.helper

import com.mobile.ads.core.nativead.MobileNativeAds
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.core.nativead.request.MobileNativeRequest
import com.mobile.ads.core.nativead.result.NativeResult

class NativeAdHelper<REQUEST : MobileNativeRequest>(strategy: MobileNativeAds<REQUEST>) :
    AdWithViewHelper<REQUEST, NativeResult, MobileNativeAdListener, MobileNativeAds<REQUEST>>(strategy) {
    override fun configResultWithListener(result: NativeResult) {
        result.listenerManager.addListener(*listListener.toTypedArray())
    }
}