package com.mobile.ads.helper

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mobile.ads.core.IMobileAdIntoView
import com.mobile.ads.core.IMobileAdRequest
import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.listener.MobileAdListener
import com.mobile.ads.preload.AdPreloadManager
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by KO Huyn on 17/04/2024.
 */
class PreloadConfig<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult, STRATEGY : IMobileAdIntoView<REQUEST, RESULT>> private constructor() {
    var canPreloadAd: Boolean = true
        private set
    var preloadKey: String? = null
        private set
    lateinit var preloadStrategy: AdPreloadManager<REQUEST, RESULT, STRATEGY>
        private set
    var canPreloadAfterShow: Boolean = false
        private set
    var canPreloadOnResume: Boolean = true
        private set

    class Builder<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult, STRATEGY : IMobileAdIntoView<REQUEST, RESULT>>(private val strategy: AdPreloadManager<REQUEST, RESULT, STRATEGY>) {
        private val preloadConfig = PreloadConfig<REQUEST, RESULT, STRATEGY>()
        fun setCanPreloadAd(canPreloadAd: Boolean) = apply {
            preloadConfig.canPreloadAd = canPreloadAd
        }

        fun setPreloadKey(preloadKey: String) = apply {
            preloadConfig.preloadKey = preloadKey
        }

        fun setCanPreloadAfterShow(canPreloadAfterShow: Boolean) = apply {
            preloadConfig.canPreloadAfterShow = canPreloadAfterShow
        }

        fun setCanPreloadOnResume(canPreloadOnResume: Boolean) = apply {
            preloadConfig.canPreloadOnResume = canPreloadOnResume
        }

        fun build(): PreloadConfig<REQUEST, RESULT, STRATEGY> {
            preloadConfig.preloadStrategy = strategy
            return preloadConfig
        }
    }
}

abstract class AdWithViewHelper<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult, LISTENER : MobileAdListener, STRATEGY : IMobileAdIntoView<REQUEST, RESULT>>(
    private var strategy: STRATEGY
) {
    private var viewGroup: ViewGroup? = null
    private var shimmerView: View? = null
    private var adResult: RESULT? = null
    private var canShowAd: Boolean = true
    protected var listListener = CopyOnWriteArrayList<LISTENER>()
    private var prevRequestAd: REQUEST? = null
    private val reloadAdWorker by lazy {
        ReloadAdWithLifecycleWorker {
            prevRequestAd?.let { requestAndShowAd(it) }
        }
    }
    private var preloadConfig: PreloadConfig<REQUEST, RESULT, STRATEGY>? = null
    private var mapListenerToSet = mutableMapOf<String, LISTENER>()
    fun addListener(listener: LISTENER) = apply {
        listListener.add(listener)
    }

    fun setListener(key: String, listener: LISTENER) = apply {
        mapListenerToSet[key] = listener
    }

    fun removeListenerByKey(key: String) = apply {
        val listener = mapListenerToSet[key]
        if (listener != null) {
            mapListenerToSet.remove(key)
        }
    }

    fun removeListener(listener: LISTENER) = apply {
        listListener.remove(listener)
    }

    fun withLifecycleOwner(lifecycle: LifecycleOwner) = apply {
        lifecycle.lifecycle.addObserver(reloadAdWorker)
    }

    fun withPreloadConfig(preloadConfig: PreloadConfig<REQUEST, RESULT, STRATEGY>) = apply {
        this.preloadConfig = preloadConfig
    }

    fun setContainerAd(viewGroup: ViewGroup) = apply {
        this.viewGroup = viewGroup
    }

    fun setCanShowAd(canShowAd: Boolean) = apply {
        this.canShowAd = canShowAd
    }

    fun setCanReloadAd(canReloadAd: Boolean) = apply {
        reloadAdWorker.setFlagActiveReload(canReloadAd)
    }

    fun setShimmerAd(view: View) = apply {
        this.shimmerView = view
    }

    fun requestAndShowAd(request: REQUEST) {
        prevRequestAd = request
        val preloadConfig = preloadConfig
        if (preloadConfig != null && preloadConfig.canPreloadAd) {
            val preloadKey = preloadConfig.preloadKey
            if (preloadKey != null) {
                val result = preloadConfig.preloadStrategy.pollAdResultByKey(preloadKey)
                if (result != null) {
                    showAd(result)
                    return
                }
            }
        }
        shimmerView?.isVisible = true
        strategy.requestAd(request, object : AdResultListener<RESULT> {
            override fun onCompleteListener(result: RESULT) {
                showAd(result)
            }

            override fun onFailureListener(error: MobileAdError) {
                shimmerView?.isVisible = false
                viewGroup?.isVisible = false
            }
        })
    }

    private fun showAd(result: RESULT) {
        shimmerView?.isVisible = false
        adResult = result
        configResultWithListener(result)
        val viewGroup = viewGroup
        if (viewGroup != null) {
            strategy.populateAd(viewGroup, result)
        }
        //PRELOAD AFTER SHOW
        val preloadConfig = preloadConfig
        if (preloadConfig != null) {
            val preloadKey = preloadConfig.preloadKey
            val prevRequest = prevRequestAd
            if (preloadConfig.canPreloadAfterShow && preloadKey != null && prevRequest != null) {
                preloadConfig.preloadStrategy.preloadByKey(preloadKey, prevRequest)
            }
        }
    }

    abstract fun configResultWithListener(result: RESULT)
}


private class ReloadAdWithLifecycleWorker(private var onExecuteRequestAd: () -> Unit) :
    DefaultLifecycleObserver {
    private val jobOwner = Handler(Looper.getMainLooper())
    private val jobExecuteReload = Runnable {
        Log.d("ReloadAdWithLifecycle", "run job request ad")
        onExecuteRequestAd()
    }
    private var hasSkipFirstResume = AtomicBoolean(false)
    private var flagActiveReload = AtomicBoolean(true)

    fun setFlagActiveReload(isActiveReload: Boolean) = apply {
        flagActiveReload.set(isActiveReload)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d("ReloadAdWithLifecycle", "onResume")
        if (hasSkipFirstResume.get() && flagActiveReload.get()) {
            jobOwner.removeCallbacks(jobExecuteReload)
            jobOwner.postDelayed(jobExecuteReload, 500)
        } else {
            hasSkipFirstResume.set(true)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        jobOwner.removeCallbacks(jobExecuteReload)
        Log.d("ReloadAdWithLifecycle", "onPause")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d("ReloadAdWithLifecycle", "onDestroy")
        owner.lifecycle.removeObserver(this)
    }
}
