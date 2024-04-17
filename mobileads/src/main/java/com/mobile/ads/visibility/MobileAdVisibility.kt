package com.mobile.ads.visibility

/**
 * Created by KO Huyn on 18/03/2024.
 */
object MobileAdVisibility {
    private var isVisibleAd = true
    fun isVisibleAd(): Boolean = isVisibleAd
    fun addCondition(condition: () -> Boolean) {
        isVisibleAd = condition()
    }
}