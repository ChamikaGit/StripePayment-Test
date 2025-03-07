package com.chami.strippaymenttest.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.chami.strippaymenttest.R
import com.chami.strippaymenttest.presentation.payment.PaymentActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, PaymentActivity::class.java))
            finish()
        }, 2000)
    }
}