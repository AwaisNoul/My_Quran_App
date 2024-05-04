package com.disc.myquranapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val number: Int = 0,
    val name: String = "",
    val englishName: String = "",
    val englishNameTranslation: String = "",
    val numberOfAyahs: Int = 0,
    val revelationType: String = ""
):Parcelable