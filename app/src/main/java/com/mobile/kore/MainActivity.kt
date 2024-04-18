package com.mobile.kore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.kohuyn.kore.databinding.ActivityMainBinding
import com.mobile.ads.admob.banner.AdmobBannerAds
import com.mobile.ads.admob.interstitial.AdmobInterstitialAds
import com.mobile.ads.admob.nativead.AdmobNativeAds
import com.mobile.ads.admob.preload.AdmobBannerAdPreload
import com.mobile.ads.admob.preload.AdmobInterstitialAdPreload
import com.mobile.ads.admob.preload.AdmobNativeAdPreload
import com.mobile.ads.admob.request.AdmobBannerRequest
import com.mobile.ads.admob.request.AdmobInterstitialRequest
import com.mobile.ads.admob.request.AdmobNativeRequest
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.core.interstitial.listener.MobileInterstitialAdListener
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.helper.BannerAdHelper
import com.mobile.ads.helper.NativeAdHelper
import com.mobile.ads.helper.PreloadConfig
import com.mobile.core.ui.base.BindingActivity
import kotlinx.coroutines.launch

class MainActivity : BindingActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.btnReadFile.setOnClickListener {
            lifecycleScope.launch {
                val interstitial =
                    AdmobInterstitialAdPreload.getAdResultByKey("INTERSTITIAL_READ_FILE")
                        ?: AdmobInterstitialAds.getAd(
                            AdmobInterstitialRequest(
                                this@MainActivity,
                                "ca-app-pub-5076265612881255/2942157170"
                            )
                        ).getResult()
                if (interstitial != null) {
                    interstitial.listenerManager.addListener(object :
                        MobileInterstitialAdListener() {
                        override fun onAdClose() {
                            super.onAdClose()
                            startActivity(Intent(this@MainActivity, ReadFileActivity::class.java))
                        }
                    })
                    AdmobInterstitialAds.populateAd(this@MainActivity, interstitial)
                } else {
                    startActivity(Intent(this@MainActivity, ReadFileActivity::class.java))
                }
            }
        }
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
            .withPreloadConfig(
                PreloadConfig.Builder(AdmobBannerAdPreload)
                    .setPreloadKey("BANNER_READ_FILE")
                    .setCanPreloadAd(true)
                    .setCanPreloadAfterShow(true)
                    .build()
            )
            .requestAndShowAd(AdmobBannerRequest(this, "ca-app-pub-5076265612881255/2736975759"))


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
            .withPreloadConfig(
                PreloadConfig.Builder(AdmobNativeAdPreload)
                    .setPreloadKey("NATIVE_FILE")
                    .setCanPreloadAd(true)
                    .setCanPreloadAfterShow(true)
                    .build()
            )
            .requestAndShowAd(
                AdmobNativeRequest(
                    this,
                    "ca-app-pub-5076265612881255/1213825300",
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
            .withPreloadConfig(
                PreloadConfig.Builder(AdmobNativeAdPreload)
                    .setPreloadKey("NATIVE_LANGUAGE")
                    .setCanPreloadAd(true)
                    .setCanPreloadAfterShow(true)
                    .build()
            )

            .requestAndShowAd(
                AdmobNativeRequest(
                    this,
                    "ca-app-pub-5076265612881255/4050057428",
                    com.mobile.ads.admob.R.layout.layout_native_no_media
                )
            )
    }
}