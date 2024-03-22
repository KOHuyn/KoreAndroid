package com.kohuyn.core.analytics

/**
 * Created by KO Huyn on 09/10/2023.
 */
object Analytics {
    private val mediator: AnalyticsMediator = AnalyticsMediator.getInstance()

    @JvmStatic
    fun track(event: String) {
        mediator.getPlugins().forEach { plugin ->
            plugin.execute(AnalyticsEvent.from(event))
        }
    }
    @JvmStatic
    fun track(eventName: String, params: HashMap<String, Any?>) {
        mediator.getPlugins().forEach { plugin ->
            plugin.execute(AnalyticsEvent.from(eventName, params))
        }
    }
    @JvmStatic
    fun track(eventName: String, vararg param: Pair<String, Any?>) {
        mediator.getPlugins().forEach { plugin ->
            plugin.execute(AnalyticsEvent.from(eventName, *param))
        }
    }
}