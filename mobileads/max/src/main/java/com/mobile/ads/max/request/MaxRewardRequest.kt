package com.mobile.ads.max.request

import android.app.Activity
import com.mobile.ads.core.reward.request.MobileRewardRequest

data class MaxRewardRequest(
    override val context: Activity,
    override val adUnitId: String,
) : MobileRewardRequest(context, adUnitId)
