package com.disc.myquranapp

import android.app.Activity
import android.widget.Toast
import androidx.core.content.ContextCompat

object Utils {

    fun statusBarColor(context: Activity, id: Int = R.color.navy) {
        context.window.statusBarColor = ContextCompat.getColor(context, id)
    }

}