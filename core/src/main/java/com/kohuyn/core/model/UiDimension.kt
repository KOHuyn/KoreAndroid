package com.kohuyn.core.model

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.Dimension

/**
 * Created by KO Huyn on 03/10/2023.
 */
sealed class UiDimension {
    abstract fun getDimension(context: Context): Float

    data class FromDimension(@Dimension val dimension: Float) : UiDimension() {
        override fun getDimension(context: Context): Float {
            return dimension
        }
    }

    data class FromDimenRes(@DimenRes val dimenRes: Int) : UiDimension() {
        override fun getDimension(context: Context): Float {
            return context.resources.getDimensionPixelOffset(dimenRes).toFloat()
        }
    }

    companion object {
        @JvmStatic
        fun from(@Dimension dimension: Float): UiDimension = FromDimension(dimension)
        @JvmStatic
        fun from(@DimenRes dimenRes: Int): UiDimension = FromDimenRes(dimenRes)
    }
}