package com.mobile.kore

import android.app.Application
import com.mobile.ads.admob.appopen.AdmobAppOpenAdResume
import com.mobile.ads.admob.initializer.AdmobInitializer
import com.mobile.ads.admob.request.AdmobAppOpenRequest
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.listener.MobileAdListenerGlobal

/**
 * Created by KO Huyn on 18/04/2024.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AdmobInitializer.initAd(this)
        AdmobAppOpenAdResume.install(this)
        AdmobAppOpenAdResume.requestAdIfAvailable(
            AdmobAppOpenRequest(
                this,
                "ca-app-pub-5076265612881255/5770333204"
            )
        )
        MobileAdListenerGlobal.addOnAdClickListener {
            AdmobAppOpenAdResume.skipShowAdResumeOneTime()
        }
    }
}