package com.mobile.ads.admob.nativead

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mobile.ads.admob.R
import com.mobile.ads.core.nativead.MobileNativeAds
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.safeResume
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.admob.nativead.listener.AdmobNativeAdListenerCollection
import com.mobile.ads.admob.request.AdmobNativeRequest
import com.mobile.ads.admob.result.AdmobNativeResult
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by KO Huyn on 05/03/2024.
 */
object AdmobNativeAds : MobileNativeAds<AdmobNativeRequest> {
    override fun requestAd(
        request: AdmobNativeRequest,
        listener: AdResultListener<NativeResult>
    ) {
        val listenerManager = AdmobNativeAdListenerCollection()
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
        val builder = AdLoader.Builder(request.context, request.adUnitId)
            .forNativeAd { nativeAd ->
                listenerManager.invokeAdListener {
                    it.onAdLoaded(
                        AdmobNativeResult(
                            nativeAd,
                            listenerManager
                        )
                    )
                }
            }.withAdListener(listenerManager.invokeTransformListener())
        builder.build().loadAd(AdRequest.Builder().build())
    }

    override suspend fun getAd(request: AdmobNativeRequest): AdResult<NativeResult> =
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

    override fun populateTo(view: ViewGroup, nativeLayoutId: Int, nativeResult: NativeResult) {
        if (nativeResult is AdmobNativeResult) {
            val nativeAd = nativeResult.native
            val nativeAdView =
                LayoutInflater.from(view.context).inflate(nativeLayoutId, null) as NativeAdView
            populateNativeAdView(nativeAd, nativeAdView)
            view.removeAllViews()
            view.addView(nativeAdView)
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as? MediaView
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        (adView.headlineView as? TextView?)?.text = nativeAd.headline
        (adView.bodyView as? TextView?)?.text = nativeAd.body
        (adView.callToActionView as? TextView?)?.text = nativeAd.callToAction
        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            adView.iconView?.background = nativeAd.icon?.drawable
            adView.iconView?.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView?.visibility = View.VISIBLE
        } else {
            (adView.priceView as TextView?)?.text = nativeAd.price
            adView.priceView?.visibility = View.VISIBLE
        }
        if (nativeAd.store == null) {
            adView.storeView?.visibility = View.VISIBLE
        } else {
            (adView.storeView as TextView?)?.text = nativeAd.store
            adView.storeView?.visibility = View.VISIBLE
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as? RatingBar?)?.rating = nativeAd.starRating?.toFloat() ?: 0f
            adView.starRatingView?.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView?)?.text = nativeAd.advertiser
            adView.advertiserView?.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }
}