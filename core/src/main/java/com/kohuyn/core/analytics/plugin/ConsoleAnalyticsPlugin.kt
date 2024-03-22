package com.kohuyn.core.analytics.plugin

import android.util.Log
import com.kohuyn.core.analytics.AnalyticsEvent
import com.kohuyn.core.analytics.plugin.AnalyticsPlugin

class ConsoleAnalyticsPlugin : AnalyticsPlugin {
    override fun execute(event: AnalyticsEvent) {
        Log.d("ConsoleAnalyticsPlugin", "Received analytics event: $event")
    }
}