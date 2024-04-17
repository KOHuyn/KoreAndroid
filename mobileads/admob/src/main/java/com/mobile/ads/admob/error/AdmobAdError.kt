package com.mobile.ads.admob.error

import com.google.android.gms.ads.AdError
import com.mobile.ads.error.MobileAdError

data class AdmobAdError(val error: AdError?) : MobileAdError()
