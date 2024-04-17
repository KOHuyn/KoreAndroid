package com.mobile.ads.listener

import com.mobile.ads.error.MobileAdError

sealed class AdResult<D> {
    data class Success<D>(val data: D) : AdResult<D>()
    data class Failure<D>(val error: MobileAdError) : AdResult<D>()

    fun getResult(): D? {
        return (this as? Success<D>)?.data
    }
}