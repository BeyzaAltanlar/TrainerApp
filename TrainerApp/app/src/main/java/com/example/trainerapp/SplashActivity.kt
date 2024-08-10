package com.example.trainerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.util.Log


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("SplashActivity", "onCreate called")

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("SplashActivity", "Starting MainActivity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000) // Delay of 5 seconds
    }

}
