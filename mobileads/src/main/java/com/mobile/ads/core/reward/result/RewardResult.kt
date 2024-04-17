package com.mobile.ads.core.reward.result

import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.reward.listener.MobileRewardAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class RewardResult(
    open val listenerManager: MobileAdListenerTransform<MobileRewardAdListener, *>
) : IMobileAdResult