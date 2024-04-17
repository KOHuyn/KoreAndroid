package com.mobile.ads.max.request

import android.content.Context
import com.mobile.ads.core.appopen.request.AppOpenRequest

data class MaxAppOpenRequest(
    override val context: Context,
    override val adUnitId: String
) : AppOpenRequest(context, adUnitId)