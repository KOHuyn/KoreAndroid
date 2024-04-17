package com.mobile.core.analytics.plugin

import android.util.Log
import com.mobile.core.analytics.AnalyticsEvent

class ConsoleAnalyticsPlugin : AnalyticsPlugin {
    override fun execute(event: AnalyticsEvent) {
        Log.d("ConsoleAnalyticsPlugin", "Received analytics event: $event")
    }
}