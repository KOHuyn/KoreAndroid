package com.mobile.ads.max.banner

import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import com.mobile.ads.core.banner.MobileBannerAds
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.max.banner.listener.MaxBannerAdListenerCollection
import com.mobile.ads.max.error.MaxAdError
import com.mobile.ads.max.request.MaxBannerRequest
import com.mobile.ads.max.result.MaxBannerResult
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object MaxBannerAds : MobileBannerAds<MaxBannerRequest> {
    override fun requestAd(
        request: MaxBannerRequest,
        listener: AdResultListener<BannerResult>
    ) {
        val adView = MaxAdView(request.adUnitId, request.activity)
        val listenerManager = MaxBannerAdListenerCollection()
        listenerManager.addMaxListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd) {
                val result = MaxBannerResult(adView, listenerManager)
                listenerManager.invokeAdListener { it.onAdLoaded(result) }
                listener.onCompleteListener(result)
            }

            override fun onAdDisplayed(p0: MaxAd) {}
            override fun onAdHidden(p0: MaxAd) {}
            override fun onAdClicked(p0: MaxAd) {}
            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                listener.onFailureListener(MaxAdError(p1))
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {}
            override fun onAdExpanded(p0: MaxAd) {}
            override fun onAdCollapsed(p0: MaxAd) {}
        })
        val isTablet = AppLovinSdkUtils.isTablet(request.activity)
        val heightPx = AppLovinSdkUtils.dpToPx(request.activity, if (isTablet) 90 else 50)
        adView.setListener(listenerManager.invokeTransformListener())
        adView.layoutParams =
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx)
        adView.loadAd()
    }

    override suspend fun getAd(request: MaxBannerRequest): AdResult<BannerResult> =
        suspendCancellableCoroutine { cott ->
            requestAd(request, object : AdResultListener<BannerResult> {
                override fun onCompleteListener(result: BannerResult) {
                    cott.safeResume(AdResult.Success(result))
                }

                override fun onFailureListener(error: MobileAdError) {
                    cott.safeResume(AdResult.Failure(error))
                }
            })
        }

    override fun populateAd(viewGroup: ViewGroup, result: BannerResult) {
        viewGroup.removeAllViews()
        viewGroup.addView(result.bannerView)
    }
}