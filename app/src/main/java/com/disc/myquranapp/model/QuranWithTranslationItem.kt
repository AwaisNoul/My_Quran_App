package com.disc.myquranapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuranWithTranslationItem(
    val id: Int = 0,
    val name: String = "",
    val total_verses: Int = 0,
    val translation: String = "",
    val transliteration: String = "",
    val type: String = "",
    val verses: List<Verse>
):Parcelable