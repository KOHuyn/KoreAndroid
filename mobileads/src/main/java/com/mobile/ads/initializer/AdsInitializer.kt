package com.mobile.ads.initializer

import android.app.Application
import com.mobile.ads.provider.AdProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * Created by KO Huyn on 05/03/2024.
 */
open class AdsInitializer {
    open fun initAd(application: Application) {
        _application = application
    }

    companion object {
        private lateinit var _application: Application
        private val _adCoroutineScope by lazy { CoroutineScope(Dispatchers.Default) }
        fun getApplicationContext(): Application {
            return if (this::_application.isInitialized) _application else throw IllegalAccessException(
                "Need initialize ad with application before call request ad"
            )
        }

        fun getAdCoroutineScope(): CoroutineScope {
            return _adCoroutineScope
        }
    }
}