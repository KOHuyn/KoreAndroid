package com.kohuyn.core.ui.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kohuyn.core.analytics.Analytics

/**
 * Created by KO Huyn on 02/10/2023.
 */
abstract class KoreActivity : AppCompatActivity() {
    protected abstract fun updateUI(savedInstanceState: Bundle?)
    protected abstract fun createContentView(savedInstanceState: Bundle?): View

    @ColorRes
    open fun getStatusBarColor(): Int {
        return android.R.color.white
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ContextCompat.getColor(this, getStatusBarColor()))
        val viewRoot = createContentView(savedInstanceState)
        setContentView(viewRoot)
        supportActionBar?.hide()
        actionBar?.hide()
        updateUI(savedInstanceState)
    }

    fun setStatusBarColor(@ColorInt color: Int) {
        if (window.statusBarColor == color) return
        window.statusBarColor = color
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = isColorLight(color)
        }
    }

    private fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    private fun isColorLight(@ColorInt color: Int): Boolean {
        return !isColorDark(color)
    }

    override fun onResume() {
        super.onResume()
//        hideNavigation(window)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        hideNavigation(window)
    }

    fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideNavigation(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
            if (windowInsetsController != null) {
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
                if (window.decorView.rootWindowInsets != null) {
                    window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(
                        WindowInsetsCompat.Type.navigationBars()
                    )
                }
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
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