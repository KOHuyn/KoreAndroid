package com.mobile.ads.core.interstitial.listener

import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.listener.MobileAdListener

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileInterstitialAdListener : MobileAdListener() {
   open fun onAdLoaded(interstitialResult: InterstitialResult) = Unit
   open fun onAdClose() = Unit
   open fun onAdFailToShow() = Unit
}