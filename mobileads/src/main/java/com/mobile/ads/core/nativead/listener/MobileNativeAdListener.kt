package com.mobile.ads.core.nativead.listener

import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.listener.MobileAdListener

abstract class MobileNativeAdListener : MobileAdListener() {
    open fun onAdLoaded(nativeResult: NativeResult) = Unit
}