package com.kohuyn.kore

import android.os.Bundle
import android.view.LayoutInflater
import com.kohuyn.core.ui.base.BindingActivity
import com.kohuyn.kore.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
    }
}