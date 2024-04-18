package com.mobile.ads.core.nativead.request

import android.content.Context
import androidx.annotation.LayoutRes
import com.mobile.ads.core.IMobileAdRequest

abstract class MobileNativeRequest(
    override val context: Context,
    override val adUnitId: String,
    open val nativeLayoutId: Int
) : IMobileAdRequest