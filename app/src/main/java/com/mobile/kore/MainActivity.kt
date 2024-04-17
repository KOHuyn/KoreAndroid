package com.mobile.kore

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.kohuyn.kore.R
import com.kohuyn.kore.databinding.ActivityMainBinding
import com.mobile.ads.admob.banner.AdmobBannerAds
import com.mobile.ads.admob.nativead.AdmobNativeAds
import com.mobile.ads.admob.request.AdmobBannerRequest
import com.mobile.ads.admob.request.AdmobNativeRequest
import com.mobile.ads.ext.onAttachShimmer
import com.mobile.ads.ext.onLoaded
import com.mobile.ads.ext.onVisibleOrInvisibleView
import com.mobile.ads.ext.requestAdAsFlow
import com.mobile.core.ui.base.BindingActivity
import kotlinx.coroutines.flow.launchIn

class MainActivity : BindingActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        repeat(10) {
            AdmobBannerAds.requestAdAsFlow(
                AdmobBannerRequest(
                    this,
                    "ca-app-pub-3940256099942544/6300978111"
                )
            )
                .onAttachShimmer(R.layout.shimmer_banner_view, binding.bannerFrame)
                .onVisibleOrInvisibleView(binding.bannerFrame)
                .onLoaded { AdmobBannerAds.populateAd(binding.bannerFrame, it) }
                .launchIn(lifecycleScope)
        }
        repeat(10) {
            AdmobNativeAds.requestAdAsFlow(
                AdmobNativeRequest(
                    this,
                    "ca-app-pub-3940256099942544/1044960115"
                )
            )
                .onAttachShimmer(R.layout.shimmer_native_small_view, binding.nativeFrame)
                .onVisibleOrInvisibleView(binding.nativeFrame)
                .onLoaded {
                    AdmobNativeAds.populateTo(
                        binding.nativeFrame,
                        com.mobile.ads.admob.R.layout.layout_native_media, it
                    )
                }
                .launchIn(lifecycleScope)
        }
        AdmobNativeAds.requestAdAsFlow(
            AdmobNativeRequest(
                this,
                "ca-app-pub-3940256099942544/2247696110"
            )
        )
            .onAttachShimmer(R.layout.shimmer_native_small_view, binding.nativeFrameNoMedia)
            .onVisibleOrInvisibleView(binding.nativeFrameNoMedia)
            .onLoaded {
                AdmobNativeAds.populateTo(
                    binding.nativeFrameNoMedia,
                    com.mobile.ads.admob.R.layout.layout_native_no_media, it
                )
            }
            .launchIn(lifecycleScope)
    }
}