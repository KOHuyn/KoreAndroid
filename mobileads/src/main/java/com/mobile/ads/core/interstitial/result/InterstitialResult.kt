package com.mobile.ads.core.interstitial.result

import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.interstitial.listener.MobileInterstitialAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class InterstitialResult(
    open val listenerManager: MobileAdListenerTransform<MobileInterstitialAdListener, *>
) : IMobileAdResult