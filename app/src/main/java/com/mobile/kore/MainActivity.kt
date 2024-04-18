package com.mobile.kore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.kohuyn.kore.R
import com.kohuyn.kore.databinding.ActivityMainBinding
import com.mobile.ads.admob.banner.AdmobBannerAds
import com.mobile.ads.admob.nativead.AdmobNativeAds
import com.mobile.ads.admob.request.AdmobBannerRequest
import com.mobile.ads.admob.request.AdmobNativeRequest
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.onAttachShimmer
import com.mobile.ads.ext.onLoaded
import com.mobile.ads.ext.onVisibleOrInvisibleView
import com.mobile.ads.ext.requestAdAsFlow
import com.mobile.ads.helper.BannerAdHelper
import com.mobile.ads.helper.NativeAdHelper
import com.mobile.core.ui.base.BindingActivity
import kotlinx.coroutines.flow.launchIn

class MainActivity : BindingActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        BannerAdHelper(AdmobBannerAds)
            .addListener(object : MobileBannerAdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d("BannerAdHelper", "onAdImpression")
                }
            })
            .setContainerAd(binding.bannerFrame)
            .setCanReloadAd(true)
            .setShimmerAd(binding.shimmerBannerAd.root)
            .withLifecycleOwner(this)
            .requestAndShowAd(AdmobBannerRequest(this, "ca-app-pub-3940256099942544/6300978111"))


        NativeAdHelper(AdmobNativeAds)
            .addListener(object : MobileNativeAdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d("NativeAdHelper", "layout_native_media onAdImpression")
                }
            })
            .setContainerAd(binding.nativeFrame)
            .setCanReloadAd(true)
            .setShimmerAd(binding.shimmerNative.root)
            .withLifecycleOwner(this)
            .requestAndShowAd(
                AdmobNativeRequest(
                    this,
                    "ca-app-pub-3940256099942544/1044960115",
                    com.mobile.ads.admob.R.layout.layout_native_media
                )
            )

        NativeAdHelper(AdmobNativeAds)
            .addListener(object : MobileNativeAdListener() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d("NativeAdHelper", "layout_native_no_media onAdImpression")
                }
            })
            .setContainerAd(binding.nativeFrameNoMedia)
            .setCanReloadAd(true)
            .setShimmerAd(binding.shimmerNativeNoMedia.root)
            .withLifecycleOwner(this)
            .requestAndShowAd(
                AdmobNativeRequest(
                    this,
                    "ca-app-pub-3940256099942544/2247696110",
                    com.mobile.ads.admob.R.layout.layout_native_no_media
                )
            )
    }
}