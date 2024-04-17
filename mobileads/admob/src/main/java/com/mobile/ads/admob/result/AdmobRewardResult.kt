package com.mobile.ads.admob.result

import com.google.android.gms.ads.rewarded.RewardedAd
import com.mobile.ads.admob.reward.listener.AdmobRewardAdListenerCollection
import com.mobile.ads.core.reward.result.RewardResult

data class AdmobRewardResult(
    val rewardedAd: RewardedAd,
    override val listenerManager: AdmobRewardAdListenerCollection
) : RewardResult(listenerManager)
