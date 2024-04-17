package com.mobile.ads.admob.reward

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.mobile.ads.admob.request.AdmobRewardRequest
import com.mobile.ads.admob.result.AdmobRewardResult
import com.mobile.ads.core.reward.MobileRewardAds
import com.mobile.ads.core.reward.listener.MobileRewardAdListener
import com.mobile.ads.core.reward.result.RewardResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.admob.reward.listener.AdmobRewardAdListenerCollection
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object AdmobRewardAds : MobileRewardAds<AdmobRewardRequest> {
    override fun requestAd(
        request: AdmobRewardRequest,
        listener: AdResultListener<RewardResult>
    ) {
        val listenerManager = AdmobRewardAdListenerCollection()
        val adRequest = AdRequest.Builder().build()
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
        RewardedAd.load(
            request.context,
            request.adUnitId,
            adRequest,
            listenerManager.invokeTransformListener().first
        )
    }

    override suspend fun getAd(request: AdmobRewardRequest): AdResult<RewardResult> =
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
        if (result is AdmobRewardResult) {
            val rewardAds = result.rewardedAd
            rewardAds.fullScreenContentCallback =
                result.listenerManager.invokeTransformListener().second
            rewardAds.show(activity) {
                result.listenerManager.invokeAdListener { it.onCollectReward() }
            }
        }
    }
}