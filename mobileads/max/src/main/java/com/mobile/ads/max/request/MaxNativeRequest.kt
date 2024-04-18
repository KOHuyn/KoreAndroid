package com.mobile.ads.max.request

import android.content.Context
import androidx.annotation.LayoutRes
import com.mobile.ads.core.nativead.request.MobileNativeRequest

data class MaxNativeRequest(
    override val context: Context,
    override val adUnitId: String,
    @LayoutRes
    override val nativeLayoutId: Int
) : MobileNativeRequest(context, adUnitId, nativeLayoutId)
