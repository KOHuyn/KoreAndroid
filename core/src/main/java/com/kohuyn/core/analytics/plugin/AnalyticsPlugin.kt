package com.kohuyn.core.analytics.plugin

import com.kohuyn.core.analytics.AnalyticsEvent

interface AnalyticsPlugin {
    fun execute(event: AnalyticsEvent)
}