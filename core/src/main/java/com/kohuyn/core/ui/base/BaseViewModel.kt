package com.kohuyn.core.ui.base

import androidx.lifecycle.ViewModel
import com.kohuyn.core.analytics.Analytics

abstract class BaseViewModel : ViewModel(){
    fun track(eventName: String) {
        Analytics.track(eventName)
    }

    fun track(eventName: String, params: HashMap<String, Any?>) {
        Analytics.track(eventName,params)
    }

    fun track(eventName: String, vararg param: Pair<String, Any?>) {
        Analytics.track(eventName,*param)
    }
}