package com.example.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.recipeapp.databinding.ActivitySplashBinding
import com.example.recipeapp.framework.applyAnimation
import com.example.recipeapp.framework.callDelayed
import com.example.recipeapp.framework.isOnline
import com.example.recipeapp.framework.setTextPreference
import com.example.recipeapp.framework.startActivity
import com.google.android.material.snackbar.Snackbar


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setTextPreference("open-api-key", "fake-key")

        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val imageView = binding.imageView

        imageView.applyAnimation(R.anim.splash_icon_animation)

        callDelayed(3000) {
            if (isOnline()) {
                startActivity<MainActivity>()
            } else {
                Snackbar.make(this, findViewById(R.id.imageView), "No internet connection", Snackbar.LENGTH_INDEFINITE).show()
            }
        }
    }


}