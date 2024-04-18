package com.mobile.ads.ext

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by KO Huyn on 17/04/2024.
 */

fun Context.isNetworkConnected(): Boolean {
    val netInfo = kotlin.runCatching {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    }.getOrNull()
    return netInfo != null && netInfo.isConnected
}