package com.mobile.ads.listener

import com.mobile.ads.error.MobileAdError

interface AdResultListener<T> {
    fun onCompleteListener(result: T)
    fun onFailureListener(error: MobileAdError)
}