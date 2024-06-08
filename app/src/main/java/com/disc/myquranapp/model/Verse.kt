package com.disc.myquranapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verse(
    val id: Int = 0,
    val text: String = "",
    val translation: String = ""
): Parcelable