package com.mobile.ads.admob.request

import android.content.Context
import com.mobile.ads.core.appopen.request.AppOpenRequest

data class AdmobAppOpenRequest(
    override val context: Context,
    override val adUnitId: String
) : AppOpenRequest(context, adUnitId)