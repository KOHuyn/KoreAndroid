package com.mobile.ads.admob.initializer

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.mobile.ads.initializer.AdsInitializer
import com.mobile.ads.provider.AdProvider


/**
 * Created by KO Huyn on 05/03/2024.
 */
object AdmobInitializer : AdsInitializer() {
    override fun initAd(application: Application) {
        MobileAds.initialize(application) { initializationStatus ->
            val statusMap =
                initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "AdmobInitializer", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
    }
}