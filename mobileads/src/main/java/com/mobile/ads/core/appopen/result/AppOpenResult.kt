package com.mobile.ads.core.appopen.result

import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.appopen.listener.MobileAppOpenAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 07/03/2024.
 */
abstract class AppOpenResult(open val listenerManager: MobileAdListenerTransform<MobileAppOpenAdListener, *>) : IMobileAdResult