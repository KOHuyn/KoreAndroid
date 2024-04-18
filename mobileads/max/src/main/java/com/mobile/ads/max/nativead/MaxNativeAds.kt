package com.mobile.ads.max.nativead

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.mobile.ads.core.nativead.MobileNativeAds
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.max.R
import com.mobile.ads.max.nativead.listener.MaxNativeAdListenerCollection
import com.mobile.ads.max.request.MaxNativeRequest
import com.mobile.ads.max.result.MaxNativeResult
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object MaxNativeAds : MobileNativeAds<MaxNativeRequest> {

    override fun requestAd(
        request: MaxNativeRequest,
        listener: AdResultListener<NativeResult>
    ) {
        val nativeAdLoader = MaxNativeAdLoader(request.adUnitId, request.context)
        val listenerManager = MaxNativeAdListenerCollection()
        listenerManager.addMaxListener(object :MaxNativeAdListener(){
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd) {
                super.onNativeAdLoaded(p0, p1)
                listenerManager.invokeAdListener {
                    it.onAdLoaded(MaxNativeResult(p0, p1, request.nativeLayoutId, listenerManager))
                }
            }
        })
        listenerManager.addListener(object : MobileNativeAdListener() {
            override fun onAdLoaded(nativeResult: NativeResult) {
                super.onAdLoaded(nativeResult)
                listener.onCompleteListener(nativeResult)
            }

            override fun onAdFailToLoad(error: MobileAdError) {
                super.onAdFailToLoad(error)
                listener.onFailureListener(error)
            }
        })
        nativeAdLoader.setNativeAdListener(listenerManager.invokeTransformListener())
        nativeAdLoader.loadAd(createNativeAdView(request.context, request.nativeLayoutId))
    }

    override suspend fun getAd(request: MaxNativeRequest): AdResult<NativeResult> =
        suspendCancellableCoroutine { cott ->
            requestAd(request, object : AdResultListener<NativeResult> {
                override fun onCompleteListener(result: NativeResult) {
                    cott.safeResume(AdResult.Success(result))
                }

                override fun onFailureListener(error: MobileAdError) {
                    cott.safeResume(AdResult.Failure(error))
                }
            })
        }

    override fun populateAd(viewGroup: ViewGroup, result: NativeResult) {
        if (result is MaxNativeResult) {
            viewGroup.removeAllViews()
            viewGroup.addView(result.maxNativeAdView)
        }
    }

    private fun createNativeAdView(
        context: Context,
        @LayoutRes
        nativeLayoutId: Int
    ): MaxNativeAdView {
        val binder = MaxNativeAdViewBinder.Builder(nativeLayoutId)
            .setTitleTextViewId(R.id.ad_headline)
            .setBodyTextViewId(R.id.ad_body)
            .setStarRatingContentViewGroupId(R.id.ad_stars)
            .setAdvertiserTextViewId(R.id.ad_advertiser)
            .setIconImageViewId(R.id.ad_app_icon)
            .setMediaContentViewGroupId(R.id.ad_media)
            .setCallToActionButtonId(R.id.ad_call_to_action)
            .build()
        return MaxNativeAdView(binder, context)
    }
}