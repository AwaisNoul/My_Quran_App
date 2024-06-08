package com.disc.myquranapp.activitys.quran

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.Utils.hideNavigationBar
import com.disc.myquranapp.model.Verse
import com.disc.myquranapp.databinding.ActivityQuranWithTranslationVerseBinding
import com.disc.myquranapp.databinding.ItemviewAyatWithTranslationBinding
import com.disc.myquranapp.setData

class QuranWithTranslationVerseActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuranWithTranslationVerseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuranWithTranslationVerseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor()
        hideNavigationBar(this)

        val verseList: List<Verse> = intent.getParcelableArrayListExtra("verse_list")!!
        binding.recyclerview.setData(verseList, ItemviewAyatWithTranslationBinding::inflate) { b, item, position ->
            b.ayatNumber.text = item.id.toString()
            b.ayat.text = item.text
            b.translation.text =" ترجمہ:"+item.translation
        }
    }
}