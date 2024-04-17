package com.mobile.ads.initializer

import android.app.Application
import com.mobile.ads.provider.AdProvider


/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class AdsInitializer {
   abstract fun initAd(application: Application)
}