package com.mobile.ads.core.nativead.request

import android.content.Context
import androidx.annotation.LayoutRes
import com.mobile.ads.core.IMobileAdRequest

abstract class MobileNativeRequest(
    open val context: Context,
    open val adUnitId: String
) : IMobileAdRequest