package com.mobile.ads.admob.banner

import android.app.Activity
import android.util.DisplayMetrics
import android.view.Display
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.mobile.ads.core.banner.MobileBannerAds
import com.mobile.ads.admob.banner.listener.AdmobBannerAdListenerCollection
import com.mobile.ads.admob.error.AdmobAdError
import com.mobile.ads.admob.request.AdmobBannerRequest
import com.mobile.ads.admob.result.AdmobBannerResult
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

/**
 * Created by KO Huyn on 05/03/2024.
 */
object AdmobBannerAds : MobileBannerAds<AdmobBannerRequest> {
    override fun requestAd(
        request: AdmobBannerRequest,
        listener: AdResultListener<BannerResult>
    ) {
        val listenerManager = AdmobBannerAdListenerCollection()
        val adView = AdView(request.activity)
        adView.adUnitId = request.adUnitId
        val adRequest: AdRequest = AdRequest.Builder()
            .build()
        val adSize = getAdSize(request.activity)
        adView.setAdSize(adSize)
        adView.loadAd(adRequest)
        listenerManager.addAdmobListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                listenerManager.invokeAdListener {
                    it.onAdLoaded(AdmobBannerResult(adView, listenerManager))
                }
                listener.onCompleteListener(AdmobBannerResult(adView, listenerManager))
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                listener.onFailureListener(AdmobAdError(p0))
            }
        })
        adView.adListener = listenerManager.invokeTransformListener()
    }

    override suspend fun getAd(request: AdmobBannerRequest): AdResult<BannerResult> =
        withContext(Dispatchers.Main) {
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
        }

    override fun populateAd(viewGroup: ViewGroup, result: BannerResult) {
        viewGroup.removeAllViews()
        viewGroup.addView(result.bannerView)
    }

    private fun getAdSize(activity: Activity): AdSize {
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }
}