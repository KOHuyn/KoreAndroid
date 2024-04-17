package com.mobile.ads.listener

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by KO Huyn on 05/03/2024.
 */
abstract class MobileAdListenerTransform<I : MobileAdListener, O> {
    private val listAdCallback = CopyOnWriteArrayList<I>()
    fun addListener(listener: I) {
        listAdCallback.add(listener)
    }

    fun removeListener(listener: I) {
        listAdCallback.remove(listener)
    }

    abstract fun invokeTransformListener(): O

    fun invokeAdListener(action: (adListener: I) -> Unit) {
        listAdCallback.forEach(action)
    }
}