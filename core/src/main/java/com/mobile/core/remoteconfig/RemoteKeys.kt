package com.mobile.core.remoteconfig

import com.mobile.core.remoteconfig.params.RemoteEnumString
import com.mobile.core.remoteconfig.params.RemoteParam

abstract class RemoteKeys(open val remoteKey: String) {
    open class BooleanKey : RemoteKeys {
        var defaultValue: Boolean
            private set

        constructor(remoteKey: String, defaultValue: Boolean) : super(remoteKey) {
            this.defaultValue = defaultValue
        }

        constructor(
            remoteKey: String,
            defaultValue: RemoteParam.BoolValue
        ) : super(remoteKey) {
            this.defaultValue = defaultValue.value
        }
    }

    open class StringKey : RemoteKeys {
        var defaultValue: String
            private set

        constructor(remoteKey: String, defaultValue: String) : super(remoteKey) {
            this.defaultValue = defaultValue
        }

        constructor(
            remoteKey: String,
            defaultValue: RemoteParam.StringValue
        ) : super(remoteKey) {
            this.defaultValue = defaultValue.value
        }
    }

    open class StringEnumKey<T : RemoteEnumString>(
        remoteKey: String, defaultValue: T
    ) : RemoteKeys(remoteKey) {
        var defaultValue: T = defaultValue
            private set
    }

    open class DoubleKey : RemoteKeys {
        var defaultValue: Double
            private set

        constructor(remoteKey: String, defaultValue: Double) : super(remoteKey) {
            this.defaultValue = defaultValue
        }

        constructor(
            remoteKey: String,
            defaultValue: RemoteParam.DoubleValue
        ) : super(remoteKey) {
            this.defaultValue = defaultValue.value
        }
    }


    open class LongKey : RemoteKeys {
        var defaultValue: Long
            private set

        constructor(remoteKey: String, defaultValue: Long) : super(remoteKey) {
            this.defaultValue = defaultValue
        }

        constructor(
            remoteKey: String,
            defaultValue: RemoteParam.LongValue
        ) : super(remoteKey) {
            this.defaultValue = defaultValue.value
        }
    }

    open class ListIntegerKey(
        override val remoteKey: String,
        val defaultValue: List<Int>
    ) : RemoteKeys(remoteKey)
}