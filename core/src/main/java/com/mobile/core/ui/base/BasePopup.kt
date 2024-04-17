package com.mobile.core.ui.base

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.kohuyn.core.R
import com.mobile.core.analytics.Analytics

/**
 * Created by KO Huyn on 17/01/2024.
 */
abstract class BasePopup<V : ViewBinding> {
    protected lateinit var binding: V
    protected abstract fun getLayoutBinding(inflater: LayoutInflater): V
    protected abstract fun updateUI(popup: PopupWindow)

    fun show(context: Context, view: View) {
        val popupWindow = PopupWindow(context)
        popupWindow.apply {
            binding = getLayoutBinding(LayoutInflater.from(context))
            updateUI(popupWindow)
            popupWindow.contentView = binding.root
            popupWindow.contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            isFocusable = true
            elevation = 20f
            setBackgroundDrawable(null)
        }

        val heightScreen = getDisplayMetrics(context).heightPixels
        val out = IntArray(2)
        view.getLocationOnScreen(out)
        val partScreen = (heightScreen * 2) / 3
        if (out[1] > partScreen) {
            popupWindow.animationStyle = R.style.DropUpSpinner
            popupWindow.showAsDropDown(
                view,
                view.width - popupWindow.contentView.measuredWidth - 50,
                view.height - popupWindow.contentView.measuredHeight
            )
        } else {
            popupWindow.animationStyle = R.style.DropDownSpinner
            popupWindow.showAsDropDown(
                view,
                view.width - popupWindow.contentView.measuredWidth - 50,
                0
            )
        }
    }

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val metrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(metrics)
        return metrics
    }

    fun track(eventName: String) {
        Analytics.track(eventName)
    }

    fun track(eventName: String, params: HashMap<String, Any?>) {
        Analytics.track(eventName,params)
    }

    fun track(eventName: String, vararg param: Pair<String, Any?>) {
        Analytics.track(eventName,*param)
    }
}