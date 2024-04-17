package com.mobile.ads.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.mobile.ads.core.IMobileAd
import com.mobile.ads.core.IMobileAdRequest
import com.mobile.ads.core.IMobileAdResult
import com.mobile.ads.error.MobileAdError
import com.mobile.ads.listener.AdResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * Created by KO Huyn on 17/04/2024.
 */


fun <REQUEST : IMobileAdRequest, RESULT : IMobileAdResult> IMobileAd<REQUEST, RESULT>.requestAdAsFlow(
    request: REQUEST
): Flow<AdResult<RESULT>> {
    return flow { emit(getAd(request)) }.flowOn(Dispatchers.Main)
}

fun <RESULT : IMobileAdResult> Flow<AdResult<RESULT>>.onLoaded(action: suspend (RESULT) -> Unit) =
    onEach {
        if (it is AdResult.Success) {
            action(it.data)
        }
    }


fun <RESULT : IMobileAdResult> Flow<AdResult<RESULT>>.onFailToLoad(action: suspend (MobileAdError) -> Unit) =
    onEach {
        if (it is AdResult.Failure) {
            action(it.error)
        }
    }

fun <RESULT : IMobileAdResult> Flow<AdResult<RESULT>>.onVisibleOrGoneView(viewGroup: ViewGroup) =
    onEach {
        when (it) {
            is AdResult.Success -> {
                viewGroup.isVisible = true
            }

            is AdResult.Failure -> {
                viewGroup.isVisible = false
            }
        }
    }

fun <RESULT : IMobileAdResult> Flow<AdResult<RESULT>>.onVisibleOrInvisibleView(viewGroup: ViewGroup) =
    onEach {
        when (it) {
            is AdResult.Success -> {
                viewGroup.isVisible = true
            }

            is AdResult.Failure -> {
                viewGroup.isInvisible = true
            }
        }
    }

fun <RESULT : IMobileAdResult> Flow<AdResult<RESULT>>.onAttachShimmer(
    @LayoutRes shimmer: Int, viewGroup: ViewGroup
): Flow<AdResult<RESULT>> {
    val shimmerView = LayoutInflater.from(viewGroup.context).inflate(shimmer, viewGroup, false)
    return onStart {
        shimmerView.isVisible = true
        viewGroup.addView(shimmerView)
    }.onEach {
        shimmerView.isVisible = false
        viewGroup.removeView(shimmerView)
    }
}