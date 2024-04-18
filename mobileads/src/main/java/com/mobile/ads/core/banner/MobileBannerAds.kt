package com.mobile.ads.core.banner

import com.mobile.ads.core.IMobileAdIntoView
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.core.banner.result.BannerResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileBannerAds<REQUEST : MobileBannerRequest> : IMobileAdIntoView<REQUEST, BannerResult>