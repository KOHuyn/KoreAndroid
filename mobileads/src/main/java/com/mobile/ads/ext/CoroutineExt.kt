package com.mobile.ads.ext

import kotlinx.coroutines.isActive
import kotlin.coroutines.Continuation

/**
 * Created by KO Huyn on 15/03/2024.
 */

fun <T> Continuation<T>.safeResume(value: T) = kotlin.runCatching {
    if (context.isActive) {
        resumeWith(Result.success(value))
    }
}