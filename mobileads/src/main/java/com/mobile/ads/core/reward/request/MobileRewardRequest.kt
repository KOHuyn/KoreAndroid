package com.mobile.ads.core.reward.request

import android.app.Activity
import com.mobile.ads.core.IMobileAdRequest

abstract class MobileRewardRequest(
    override val context: Activity,
    override val adUnitId: String,
) : IMobileAdRequest