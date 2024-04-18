package com.mobile.ads.preload

import android.util.Log
import com.mobile.ads.core.IMobileAd
import com.mobile.ads.core.IMobileAdIntoView
import com.mobile.ads.core.IMobileAdRequest
import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.core.banner.MobileBannerAds
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.core.interstitial.MobileInterstitialAds
import com.mobile.ads.core.interstitial.request.MobileInterstitialRequest
import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.core.nativead.MobileNativeAds
import com.mobile.ads.core.nativead.request.MobileNativeRequest
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.core.reward.MobileRewardAds
import com.mobile.ads.core.reward.request.MobileRewardRequest
import com.mobile.ads.core.reward.result.RewardResult
import com.mobile.ads.initializer.AdsInitializer
import kotlinx.coroutines.launch

/**
 * Created by KO Huyn on 18/04/2024.
 */

enum class AdPreloadState {
    Loading, Complete
}

abstract class NativeAdPreload<REQUEST : MobileNativeRequest>(strategy: MobileNativeAds<REQUEST>) :
    AdPreloadManager<REQUEST, NativeResult, MobileNativeAds<REQUEST>>(strategy)

abstract class BannerAdPreload<REQUEST : MobileBannerRequest>(strategy: MobileBannerAds<REQUEST>) :
    AdPreloadManager<REQUEST, BannerResult, MobileBannerAds<REQUEST>>(strategy)

abstract class InterstitialAdPreload<REQUEST : MobileInterstitialRequest>(strategy: MobileInterstitialAds<REQUEST>) :
    AdPreloadManager<REQUEST, InterstitialResult, MobileInterstitialAds<REQUEST>>(strategy)

abstract class RewardAdPreload<REQUEST : MobileRewardRequest>(strategy: MobileRewardAds<REQUEST>) :
    AdPreloadManager<REQUEST, RewardResult, MobileRewardAds<REQUEST>>(strategy)

abstract class AdPreloadManager<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult, STRATEGY : IMobileAd<REQUEST, RESULT>>(
    private var adStrategy: STRATEGY
) {
    companion object {
        private const val TAG = "AdPreloadManager"
    }

    private val cacheAd = mutableMapOf<String, ArrayDeque<RESULT>>()
    private val cacheAdState = mutableMapOf<String, AdPreloadState>()

    fun preloadByKey(key: String, request: REQUEST) {
        AdsInitializer.getAdCoroutineScope().launch {
            if (cacheAdState[key] == null) {
                cacheAdState[key] = AdPreloadState.Loading
            }
            Log.d(TAG, "Preload request($request) with key($key)")
            val result = adStrategy.getAd(request).getResult()
            Log.d(TAG, "Preload result($result) with key($key)")
            if (result != null) {
                val queueResult = cacheAd[key] ?: ArrayDeque()
                queueResult.addLast(result)
                cacheAd[key] = queueResult
            }
            cacheAdState[key] = AdPreloadState.Complete
        }
    }

    fun pollAdResultByKey(key: String): RESULT? {
        val queueResult = cacheAd[key] ?: ArrayDeque()
        val result = queueResult.removeFirstOrNull()
        cacheAd[key] = queueResult
        return result
    }

    fun getStatePreload(key: String): AdPreloadState? {
        return cacheAdState[key]
    }

    fun getAdResultByKey(key: String): RESULT? {
        val queueResult = cacheAd[key] ?: ArrayDeque()
        return queueResult.firstOrNull()
    }
}