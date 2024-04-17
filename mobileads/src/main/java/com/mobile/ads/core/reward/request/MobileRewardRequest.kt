package com.mobile.ads.core.reward.request

import android.app.Activity
import com.mobile.ads.core.IMobileAdRequest

abstract class MobileRewardRequest(
    open val context: Activity,
    open val adUnitId: String,
) : IMobileAdRequest