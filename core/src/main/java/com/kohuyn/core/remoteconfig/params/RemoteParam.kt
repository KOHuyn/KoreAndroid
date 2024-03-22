package com.kohuyn.core.remoteconfig.params

/**
 * Created by KO Huyn on 29/09/2023.
 */
sealed class RemoteParam {
    sealed class StringValue(val value: String)
    sealed class BoolValue(val value: Boolean)
    sealed class LongValue(val value: Long)
    sealed class DoubleValue(val value: Double)
}