package com.mobile.ads.core.reward.listener

import com.mobile.ads.core.reward.result.RewardResult
import com.mobile.ads.listener.MobileAdListener

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileRewardAdListener : MobileAdListener() {
    open fun onAdLoaded(rewardResult: RewardResult) = Unit
    open fun onAdClose() = Unit
    open fun onAdFailToShow() = Unit
    open fun onCollectReward() = Unit
}