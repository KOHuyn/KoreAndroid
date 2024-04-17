package com.mobile.ads.core.interstitial

import com.mobile.ads.core.IMobileAdFullScreen
import com.mobile.ads.core.interstitial.request.MobileInterstitialRequest
import com.mobile.ads.core.interstitial.result.InterstitialResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileInterstitialAds<REQUEST : MobileInterstitialRequest> :
    IMobileAdFullScreen<REQUEST, InterstitialResult>