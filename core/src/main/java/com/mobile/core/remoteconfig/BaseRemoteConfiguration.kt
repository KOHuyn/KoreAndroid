package com.mobile.core.remoteconfig

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.LruCache
import androidx.core.content.edit
import com.mobile.core.remoteconfig.params.RemoteEnumString
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Created by KO Huyn on 25/09/2023.
 */
abstract class BaseRemoteConfiguration {

    private lateinit var application: Application
    val cacheRemote = LruCache<String, Any>(Int.MAX_VALUE)
    abstract fun getPrefsName(): String

    abstract fun sync(remoteConfig: FirebaseRemoteConfig)

    fun init(application: Application) {
        this.application = application
    }

    fun getPrefs(): SharedPreferences {
        return application.getSharedPreferences(getPrefsName(), Context.MODE_PRIVATE)
    }

    fun FirebaseRemoteConfig.saveToLocal(keyType: RemoteKeys) {
        val hasRemoteKey = kotlin.runCatching {
            getString(keyType.remoteKey).isNotEmpty()
        }.getOrDefault(true)
        if (!hasRemoteKey) return
        val remoteConfig = this
        getPrefs().edit {
            val key = keyType.remoteKey
            when (keyType) {
                is RemoteKeys.BooleanKey -> {
                    putBoolean(
                        key,
                        kotlin.runCatching {
                            remoteConfig.getBoolean(key)
                        }.getOrElse { keyType.defaultValue }
                    )
                }

                is RemoteKeys.StringKey -> {
                    putString(
                        key,
                        kotlin.runCatching {
                            remoteConfig.getString(key)
                        }.getOrElse { keyType.defaultValue })
                }

                is RemoteKeys.DoubleKey -> {
                    putFloat(
                        key,
                        kotlin.runCatching {
                            remoteConfig.getDouble(key)
                        }.getOrElse { keyType.defaultValue }.toFloat()
                    )
                }

                is RemoteKeys.LongKey -> {
                    putLong(
                        key,
                        kotlin.runCatching {
                            remoteConfig.getLong(key)
                        }.getOrElse { keyType.defaultValue })
                }

                is RemoteKeys.ListIntegerKey -> {
                    putString(key, kotlin.runCatching {
                        remoteConfig.getString(key)
                    }.getOrElse { keyType.defaultValue.joinToString(",") })
                }

                is RemoteKeys.StringEnumKey<*> -> {
                    putString(
                        key,
                        kotlin.runCatching {
                            remoteConfig.getString(key)
                        }.getOrElse { keyType.defaultValue.remoteValue })
                }

                else -> Unit
            }
        }
    }

    fun RemoteKeys.StringKey.cacheOrGet(): String {
        return kotlin.runCatching { cacheRemote[this.remoteKey] as String }.getOrElse { get() }
    }

    inline fun <reified T> RemoteKeys.StringEnumKey<T>.cacheOrGet(): T where T : RemoteEnumString, T : Enum<T> {
        return kotlin.runCatching {
            enumValues<T>().find { it.remoteValue == (cacheRemote[remoteKey] as String) }
        }.getOrNull() ?: get()
    }

    fun RemoteKeys.BooleanKey.cacheOrGet(): Boolean {
        return kotlin.runCatching { cacheRemote[remoteKey] as Boolean }.getOrElse { get() }
    }

    fun RemoteKeys.LongKey.cacheOrGet(): Long {
        return kotlin.runCatching { cacheRemote[remoteKey] as Long }.getOrElse { get() }
    }

    fun RemoteKeys.ListIntegerKey.cacheOrGet(): List<Int> {
        return kotlin.runCatching { cacheRemote[remoteKey] as List<Int> }.getOrElse { get() }
    }

    fun RemoteKeys.DoubleKey.cacheOrGet(): Double {
        return kotlin.runCatching { cacheRemote[remoteKey] as Double }.getOrElse { get() }
    }

    fun RemoteKeys.StringKey.get(): String {
        return getPrefs().getString(remoteKey, defaultValue).takeUnless { it.isNullOrBlank() }
            ?: defaultValue
    }

    inline fun <reified T> RemoteKeys.StringEnumKey<T>.get(): T where T : RemoteEnumString, T : Enum<T> {
        return kotlin.runCatching {
            val stringValue = getPrefs().getString(remoteKey, defaultValue.remoteValue)
                .takeUnless { it.isNullOrBlank() } ?: defaultValue.remoteValue
            enumValues<T>().find { it.remoteValue == stringValue } ?: defaultValue
        }.getOrNull() ?: defaultValue
    }

    fun RemoteKeys.BooleanKey.get(): Boolean {
        return getPrefs().getBoolean(remoteKey, defaultValue)
    }

    fun RemoteKeys.LongKey.get(): Long {
        return getPrefs().getLong(remoteKey, defaultValue)
    }

    fun RemoteKeys.ListIntegerKey.get(): List<Int> {
        return kotlin.runCatching {
            getPrefs().getString(remoteKey, defaultValue.joinToString(","))
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
        }.getOrNull() ?: defaultValue
    }

    fun RemoteKeys.DoubleKey.get(): Double {
        return let {
            getPrefs().getFloat(remoteKey, defaultValue.toFloat())
        }.toDouble()
    }
}