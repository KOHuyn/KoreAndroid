package com.mobile.kore

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.kohuyn.kore.R

/**
 * Created by KO Huyn on 18/04/2024.
 */
class ReadFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_read_file)
    }
}