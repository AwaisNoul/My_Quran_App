package com.disc.myquranapp

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

object Utils {

    fun systemNavigationColor(context: Activity, id: Int = R.color.navy){
        context.window.navigationBarColor = ContextCompat.getColor(context,id)
    }


    fun hideNavigationBar(context: Activity){
        context.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}