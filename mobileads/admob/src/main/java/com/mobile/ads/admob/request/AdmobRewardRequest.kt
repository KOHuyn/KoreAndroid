package com.mobile.ads.admob.request

import android.app.Activity
import com.mobile.ads.core.reward.request.MobileRewardRequest

data class AdmobRewardRequest(
    override val context: Activity,
    override val adUnitId: String,
) : MobileRewardRequest(context, adUnitId)