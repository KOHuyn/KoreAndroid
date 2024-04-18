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
import com.mobile.ads.core.banner.MobileBannerAds
import com.mobile.ads.core.banner.listener.MobileBannerAdListener
import com.mobile.ads.core.banner.request.MobileBannerRequest
import com.mobile.ads.core.banner.result.BannerResult
import com.mobile.ads.core.nativead.MobileNativeAds
import com.mobile.ads.core.nativead.listener.MobileNativeAdListener
import com.mobile.ads.core.nativead.request.MobileNativeRequest
import com.mobile.ads.core.nativead.result.NativeResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.listener.AdResultListener
import com.mobile.ads.listener.MobileAdListener
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by KO Huyn on 17/04/2024.
 */
abstract class AdWithViewHelper<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult, LISTENER : MobileAdListener, STRATEGY : IMobileAdIntoView<REQUEST, RESULT>>(
    private var strategy: STRATEGY
) {
    private var lifecycle: LifecycleOwner? = null
    private var viewGroup: ViewGroup? = null
    private var shimmerView: View? = null
    private var adResult: RESULT? = null
    private var canShowAd: Boolean = true
    private var canReloadAd: Boolean = true
    private var canPreloadAd: Boolean = false
    protected var listListener = CopyOnWriteArrayList<LISTENER>()
    private var prevRequestAd: REQUEST? = null
    private val reloadAdWorker by lazy {
        ReloadAdWithLifecycleWorker {
            prevRequestAd?.let { requestAndShowAd(it) }
        }
    }
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
        this.lifecycle = lifecycle
        lifecycle.lifecycle.addObserver(reloadAdWorker)
    }

    fun setContainerAd(viewGroup: ViewGroup) = apply {
        this.viewGroup = viewGroup
    }

    fun setCanShowAd(canShowAd: Boolean) = apply {
        this.canShowAd = canShowAd
    }

    fun setCanReloadAd(canReloadAd: Boolean) = apply {
        this.canReloadAd = canReloadAd
        reloadAdWorker.setFlagActiveReload(canReloadAd)
    }

    fun setCanPreloadAd(canPreloadAd: Boolean) = apply {
        this.canPreloadAd = canPreloadAd
    }

    fun setShimmerAd(view: View) = apply {
        this.shimmerView = view
    }

    fun requestAndShowAd(request: REQUEST) {
        prevRequestAd = request
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
    }

    abstract fun configResultWithListener(result: RESULT)
}


private class ReloadAdWithLifecycleWorker(private var onExecuteRequestAd: () -> Unit) :
    DefaultLifecycleObserver {
    private val jobOwner = Handler(Looper.getMainLooper())
    private val jobExecuteReload = Runnable {
        Log.d("ReloadAdWithLifecycle","run job request ad")
        onExecuteRequestAd()
    }
    private var hasSkipFirstResume = AtomicBoolean(false)
    private var flagActiveReload = AtomicBoolean(true)

    fun setFlagActiveReload(isActiveReload: Boolean) = apply {
        flagActiveReload.set(isActiveReload)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d("ReloadAdWithLifecycle","onResume")
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
        Log.d("ReloadAdWithLifecycle","onPause")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d("ReloadAdWithLifecycle","onDestroy")
        owner.lifecycle.removeObserver(this)
    }
}
