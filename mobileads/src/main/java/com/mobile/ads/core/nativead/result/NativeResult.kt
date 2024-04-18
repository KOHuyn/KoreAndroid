package com.mobile.ads.core.nativead.result

import androidx.annotation.LayoutRes
import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class NativeResult(
    open val listenerManager: MobileAdListenerTransform<MobileNativeAdListener, *>,
    @LayoutRes
    open val layoutNativeId: Int
) : IMobileAdResult