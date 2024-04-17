package com.mobile.ads.max.error

import com.applovin.mediation.MaxError
import com.mobile.ads.error.MobileAdError

data class MaxAdError(val error: MaxError?) : MobileAdError()
