package com.mobile.ads.core.banner.result

import android.view.View
import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class BannerResult(
    val bannerView: View,
    val listener: MobileAdListenerTransform<MobileBannerAdListener, *>
) : IMobileAdResult