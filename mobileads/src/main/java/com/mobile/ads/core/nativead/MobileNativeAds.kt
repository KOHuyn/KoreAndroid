package com.mobile.ads.core.nativead

import com.mobile.ads.core.IMobileAdIntoView
import com.mobile.ads.core.nativead.request.MobileNativeRequest
import com.mobile.ads.core.nativead.result.NativeResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileNativeAds<REQUEST : MobileNativeRequest> : IMobileAdIntoView<REQUEST, NativeResult>