package com.kohuyn.core.analytics

import com.kohuyn.core.analytics.plugin.AnalyticsPlugin

/**
 * Created by KO Huyn on 25/09/2023.
 */
class AnalyticsMediator private constructor() {
    private val plugins: MutableList<AnalyticsPlugin> = mutableListOf()
    fun addPlugin(plugin: AnalyticsPlugin) = apply {
        plugins.add(plugin)
    }
    fun addPlugin(vararg plugin: AnalyticsPlugin) = apply {
        plugins.addAll(plugin)
    }

    fun removePlugin(plugin: AnalyticsPlugin) = apply {
        plugins.remove(plugin)
    }

    fun removePlugin(plugin: Class<AnalyticsPlugin>) = apply {
        plugins.removeAll { it.javaClass == plugin }
    }

    fun getPlugins(): List<AnalyticsPlugin> {
        return plugins
    }

    companion object {
        @Volatile
        private var _instance: AnalyticsMediator? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): AnalyticsMediator = synchronized(this) {
            _instance ?: AnalyticsMediator().also { _instance = it }
        }
    }
}