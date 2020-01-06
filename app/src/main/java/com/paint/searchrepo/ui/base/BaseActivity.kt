package com.paint.searchrepo.ui.base

import android.content.Intent
import android.os.Bundle
import com.paint.searchrepo.R
import dagger.android.support.DaggerAppCompatActivity

class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //don't start the app again from icon on launcher
        if (!isTaskRoot) {
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        setContentView(R.layout.activity_base)
    }
}
