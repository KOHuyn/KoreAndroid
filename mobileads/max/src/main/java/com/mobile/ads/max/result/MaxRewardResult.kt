package com.mobile.ads.max.result

import com.applovin.mediation.ads.MaxRewardedAd
import com.mobile.ads.core.reward.result.RewardResult
import com.mobile.ads.max.reward.listener.MaxRewardAdListenerCollection

data class MaxRewardResult(
    val rewardedAd: MaxRewardedAd,
    override val listenerManager: MaxRewardAdListenerCollection
) : RewardResult(listenerManager)
