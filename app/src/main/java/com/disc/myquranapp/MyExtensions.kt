package com.disc.myquranapp

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment


object MyExtensions {


    fun Activity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Activity.statusBarColor(color: Int = R.color.navy) {
        window.statusBarColor = getColor(color)
    }

}