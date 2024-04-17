package com.mobile.core.ui.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mobile.core.model.UiDimension

/**
 * Created by KO Huyn on 16/10/2023.
 */

sealed class MarginItemDecoration(
    open val spaceHeight: UiDimension
) : RecyclerView.ItemDecoration() {

    sealed class Linear(
        override val spaceHeight: UiDimension
    ) : MarginItemDecoration(spaceHeight) {
        class Vertical(override val spaceHeight: UiDimension) : Linear(spaceHeight) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = spaceHeight.getDimension(parent.context).toInt()
                }
                outRect.bottom = spaceHeight.getDimension(parent.context).toInt()
            }
        }

        class Horizontal(override val spaceHeight: UiDimension) : Linear(spaceHeight) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = spaceHeight.getDimension(parent.context).toInt()
                }
                outRect.right = spaceHeight.getDimension(parent.context).toInt()
            }
        }
    }

    class Grid(override val spaceHeight: UiDimension, val spanCount: Int) :
        MarginItemDecoration(spaceHeight) {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val posItem = parent.getChildAdapterPosition(view) + 1
            if (posItem % spanCount != 0) {
                outRect.right = spaceHeight.getDimension(parent.context).toInt()
            }
            if (posItem <= spanCount) {
                outRect.top = spaceHeight.getDimension(parent.context).toInt()
            }
            outRect.bottom = spaceHeight.getDimension(parent.context).toInt()
        }
    }
}
