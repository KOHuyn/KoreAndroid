package com.mobile.ads.core

import android.app.Activity
import com.mobile.ads.listener.AdResult
import com.mobile.ads.listener.AdResultListener

/**
 * Created by KO Huyn on 19/03/2024.
 */
interface IMobileAdRequest
interface IMobileAdResult

interface IMobileAd<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult> {
    fun requestAd(request: REQUEST, listener: AdResultListener<RESULT>)
    suspend fun getAd(request: REQUEST): AdResult<RESULT>
}

interface IMobileAdFullScreen<REQUEST : IMobileAdRequest, RESULT : IMobileAdResult> :
    IMobileAd<REQUEST, RESULT> {
    fun populateAd(activity: Activity, result: RESULT)
}