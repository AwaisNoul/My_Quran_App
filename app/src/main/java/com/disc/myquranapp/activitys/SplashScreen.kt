package com.disc.myquranapp.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.disc.myquranapp.MainActivity
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.R
import com.disc.myquranapp.Utils.hideNavigationBar
import com.disc.myquranapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.gray)
        hideNavigationBar(this)


        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)

    }
}