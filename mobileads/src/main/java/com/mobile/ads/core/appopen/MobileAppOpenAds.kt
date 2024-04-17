package com.mobile.ads.core.appopen

import com.mobile.ads.core.IMobileAdFullScreen
import com.mobile.ads.core.appopen.request.AppOpenRequest
import com.mobile.ads.core.appopen.result.AppOpenResult

/**
 * Created by KO Huyn on 05/03/2024.
 */

interface MobileAppOpenAds<REQUEST : AppOpenRequest> : IMobileAdFullScreen<REQUEST, AppOpenResult>