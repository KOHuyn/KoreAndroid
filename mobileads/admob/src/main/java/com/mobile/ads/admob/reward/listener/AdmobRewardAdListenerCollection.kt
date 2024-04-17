package com.mobile.ads.admob.reward.listener

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.result.AdmobRewardResult
import com.mobile.ads.core.reward.listener.MobileRewardAdListener
import com.mobile.ads.listener.MobileAdListenerTransform

/**
 * Created by KO Huyn on 05/03/2024.
 */
class AdmobRewardAdListenerCollection :
    MobileAdListenerTransform<MobileRewardAdListener, Pair<RewardedAdLoadCallback, FullScreenContentCallback>>() {
    private val interstitialAdLoadCallback = object : RewardedAdLoadCallback() {
        override fun onAdLoaded(p0: RewardedAd) {
            super.onAdLoaded(p0)
            invokeAdListener { it.onAdLoaded(AdmobRewardResult(p0,this@AdmobRewardAdListenerCollection)) }
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            invokeAdListener { it.onAdFailToLoad(AdmobAdError(p0)) }
        }
    }
    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
            invokeAdListener { it.onAdClick() }
        }

        override fun onAdImpression() {
            super.onAdImpression()
            invokeAdListener { it.onAdImpression() }
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            invokeAdListener { it.onAdClose() }
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            invokeAdListener {
                it.onAdFailToShow()
                it.onAdClose()
            }
        }
    }

    override fun invokeTransformListener(): Pair<RewardedAdLoadCallback, FullScreenContentCallback> {
        return interstitialAdLoadCallback to fullScreenContentCallback
    }
}