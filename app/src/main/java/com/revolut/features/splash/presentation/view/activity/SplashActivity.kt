package com.revolut.features.splash.presentation.view.activity

import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.revolut.R
import com.revolut.features.rates.presentation.view.activity.RateActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            RateActivity.startMa(this)
        }, 3000)
    }

}