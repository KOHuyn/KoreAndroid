package com.mobile.ads.max.reward

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.mobile.ads.core.reward.MobileRewardAds
import com.mobile.ads.core.reward.listener.MobileRewardAdListener
import com.mobile.ads.core.reward.request.MobileRewardRequest
import com.mobile.ads.core.reward.result.RewardResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.max.request.MaxRewardRequest
import com.mobile.ads.max.result.MaxRewardResult
import com.mobile.ads.max.reward.listener.MaxRewardAdListenerCollection
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object MaxRewardAds : MobileRewardAds<MaxRewardRequest> {

    override fun requestAd(
        request: MaxRewardRequest,
        listener: AdResultListener<RewardResult>
    ) {
        val listenerManager = MaxRewardAdListenerCollection()
        val reward = MaxRewardedAd.getInstance(request.adUnitId, request.context)
        listenerManager.addListener(object : MobileRewardAdListener() {
            override fun onAdLoaded(rewardResult: RewardResult) {
                super.onAdLoaded(rewardResult)
                listener.onCompleteListener(rewardResult)
            }

            override fun onAdFailToLoad(error: MobileAdError) {
                super.onAdFailToLoad(error)
                listener.onFailureListener(error)
            }
        })
        listenerManager.addMaxListener(object : MaxRewardedAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                listenerManager.invokeAdListener {
                    it.onAdLoaded(MaxRewardResult(reward, listenerManager))
                }
            }

            override fun onAdDisplayed(p0: MaxAd) {}
            override fun onAdHidden(p0: MaxAd) {}
            override fun onAdClicked(p0: MaxAd) {}
            override fun onAdLoadFailed(p0: String, p1: MaxError) {}
            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {}
            override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {}

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoStarted(p0: MaxAd) {
            }

            @Deprecated("Deprecated in Java")
            override fun onRewardedVideoCompleted(p0: MaxAd) {
            }
        })
        reward.setListener(listenerManager.invokeTransformListener())
        reward.loadAd()
    }

    override suspend fun getAd(request: MaxRewardRequest): AdResult<RewardResult> =
        suspendCancellableCoroutine { cott ->
            requestAd(request, object : AdResultListener<RewardResult> {
                override fun onCompleteListener(result: RewardResult) {
                    cott.safeResume(AdResult.Success(result))
                }

                override fun onFailureListener(error: MobileAdError) {
                    cott.safeResume(AdResult.Failure(error))
                }
            })
        }

    override fun populateAd(activity: Activity, result: RewardResult) {
        if (result is MaxRewardResult) {
            val rewardedAd = result.rewardedAd
            if (rewardedAd.isReady) {
                rewardedAd.showAd()
            }
        }
    }
}