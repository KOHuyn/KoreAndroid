package com.mobile.core.analytics

sealed class AnalyticsEvent(open val eventName: String) {
    data class OnlyEvent(override val eventName: String) : AnalyticsEvent(eventName){
        override fun toString(): String {
            return "eventName($eventName)"
        }
    }
    data class Event(
        override val eventName: String,
        val params: Map<String, Any?>
    ) : AnalyticsEvent(eventName){
        override fun toString(): String {
            return "eventName($eventName), params(${params.entries.joinToString(",") { (key, value) -> "$key:$value" }})"
        }
    }

    companion object {
        fun from(eventName: String): AnalyticsEvent {
            return OnlyEvent(eventName)
        }

        fun from(eventName: String, params: HashMap<String, Any?>): AnalyticsEvent {
            return Event(eventName, params)
        }

        fun from(eventName: String, vararg param: Pair<String, Any?>): AnalyticsEvent {
            return Event(eventName, param.toMap())
        }
    }
}