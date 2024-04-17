package com.mobile.ads.core.nativead

import android.view.ViewGroup
import com.mobile.ads.core.IMobileAd
import com.mobile.ads.core.nativead.request.MobileNativeRequest
import com.mobile.ads.core.nativead.result.NativeResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileNativeAds<REQUEST : MobileNativeRequest> : IMobileAd<REQUEST, NativeResult> {
    fun populateTo(view: ViewGroup, nativeLayoutId: Int, nativeResult: NativeResult)
}

