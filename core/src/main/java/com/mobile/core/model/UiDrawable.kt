package com.mobile.core.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Created by KO Huyn on 16/12/2023.
 */
sealed class UiDrawable {
    abstract fun getDrawable(context: Context): Drawable?

    data class FromDrawable(val drawable: Drawable?) : UiDrawable() {
        override fun getDrawable(context: Context): Drawable? {
            return drawable
        }
    }

    data class FromDrawableRes(@DrawableRes val drawableRes: Int) : UiDrawable() {
        override fun getDrawable(context: Context): Drawable? {
            return ContextCompat.getDrawable(context, drawableRes)
        }
    }

    companion object {
        @JvmStatic
        fun from(drawable: Drawable?): UiDrawable = FromDrawable(drawable)
        @JvmStatic
        fun from(@DrawableRes drawableRes: Int): UiDrawable = FromDrawableRes(drawableRes)
    }
}