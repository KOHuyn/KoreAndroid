package com.mobile.ads.admob.request

import android.content.Context
import com.mobile.ads.core.nativead.request.MobileNativeRequest

data class AdmobNativeRequest(
    override val context: Context,
    override val adUnitId: String,
    override val nativeLayoutId: Int
) : MobileNativeRequest(context, adUnitId, nativeLayoutId)