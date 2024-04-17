package com.mobile.core.analytics.plugin

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobile.core.analytics.AnalyticsEvent

/**
 * Created by KO Huyn on 25/07/2023.
 */
class FirebaseAnalyticsPlugin(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsPlugin {
    override fun execute(event: AnalyticsEvent) {
        val bundle = if (event is AnalyticsEvent.Event) {
            bundleOf(*event.params.map { (k, v) -> k to (v ?: k) }.toList().toTypedArray())
        } else null
        firebaseAnalytics.logEvent(event.eventName, bundle)
    }
}
