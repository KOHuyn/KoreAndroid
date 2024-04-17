package com.mobile.core.analytics.plugin

import com.mobile.core.analytics.AnalyticsEvent

interface AnalyticsPlugin {
    fun execute(event: AnalyticsEvent)
}