package com.mobile.ads.core.interstitial.request

import android.app.Activity
import com.mobile.ads.core.IMobileAdRequest

/**
 * Created by KO Huyn on 19/03/2024.
 */
abstract class MobileInterstitialRequest(
    open val activity: Activity,
    open val adUnitId: String
) : IMobileAdRequest