package com.mobile.ads.listener

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 18/04/2024.
 */
object MobileAdListenerGlobal {

    fun interface OnClickAdListener {
        fun setOnClickAdListener()
    }

    private val listenerManager = CopyOnWriteArrayList<OnClickAdListener>()
    fun addOnAdClickListener(adClickListener: OnClickAdListener) {
        listenerManager.add(adClickListener)
    }

    internal fun invokeListener(action: (OnClickAdListener) -> Unit) {
        listenerManager.forEach(action)
    }
}