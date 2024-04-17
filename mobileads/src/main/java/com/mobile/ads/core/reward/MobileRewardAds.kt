package com.mobile.ads.core.reward

import com.mobile.ads.core.IMobileAdFullScreen
import com.mobile.ads.core.reward.request.MobileRewardRequest
import com.mobile.ads.core.reward.result.RewardResult

/**
 * Created by KO Huyn on 05/03/2024.
 */
interface MobileRewardAds<REQUEST : MobileRewardRequest> : IMobileAdFullScreen<REQUEST, RewardResult>