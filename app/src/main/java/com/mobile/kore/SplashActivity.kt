package com.mobile.kore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kohuyn.kore.R
import com.mobile.ads.admob.interstitial.AdmobInterstitialAds
import com.mobile.ads.admob.preload.AdmobBannerAdPreload
import com.mobile.ads.admob.preload.AdmobInterstitialAdPreload
import com.mobile.ads.admob.preload.AdmobNativeAdPreload
import com.mobile.ads.admob.request.AdmobBannerRequest
import com.mobile.ads.admob.request.AdmobInterstitialRequest
import com.mobile.ads.admob.request.AdmobNativeRequest
import com.mobile.ads.core.interstitial.listener.MobileInterstitialAdListener
import com.mobile.ads.core.interstitial.result.InterstitialResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.ext.onFailToLoad
import com.mobile.ads.ext.onLoaded
import com.mobile.ads.ext.requestAdAsFlow
import com.mobile.ads.listener.AdResultListener
import kotlinx.coroutines.flow.launchIn

/**
 * Created by KO Huyn on 18/04/2024.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AdmobNativeAdPreload.preloadByKey("NATIVE_FILE", AdmobNativeRequest(
            this,
            "ca-app-pub-5076265612881255/1213825300",
            com.mobile.ads.admob.R.layout.layout_native_media
        ))
        AdmobNativeAdPreload.preloadByKey(
            "NATIVE_LANGUAGE", AdmobNativeRequest(
            this,
            "ca-app-pub-5076265612881255/4050057428",
            com.mobile.ads.admob.R.layout.layout_native_no_media
        ))
        AdmobBannerAdPreload.preloadByKey(
            "BANNER_READ_FILE",
            AdmobBannerRequest(this, "ca-app-pub-5076265612881255/2736975759")
        )
        AdmobInterstitialAdPreload.preloadByKey(
            "INTERSTITIAL_READ_FILE",
            AdmobInterstitialRequest(this, "ca-app-pub-5076265612881255/2942157170")
        )
        AdmobInterstitialAds.requestAd(
            AdmobInterstitialRequest(
                this,
                "ca-app-pub-5076265612881255/5827340817"
            ), object : AdResultListener<InterstitialResult> {
                override fun onCompleteListener(result: InterstitialResult) {
                    result.listenerManager.addListener(object : MobileInterstitialAdListener() {
                        override fun onAdClose() {
                            super.onAdClose()
                            startMain()
                        }
                    })
                    AdmobInterstitialAds.populateAd(this@SplashActivity, result)
                }

                override fun onFailureListener(error: MobileAdError) {
                    startMain()
                }
            }
        )
    }

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}