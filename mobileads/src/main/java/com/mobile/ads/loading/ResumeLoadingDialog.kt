package com.mobile.ads.loading

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams

/**
 * Created by KO Huyn on 18/03/2024.
 */
class ResumeLoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FrameLayout(context)
        root.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        root.setBackgroundColor(Color.WHITE)
        val progress = ProgressBar(context)
        root.addView(progress)
        setContentView(root)
    }
}