package com.disc.myquranapp.activitys.quran

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.R
import com.disc.myquranapp.Utils.hideNavigationBar
import com.disc.myquranapp.databinding.ActivitySurahBinding
import com.disc.myquranapp.databinding.AyatItemviewBinding
import com.disc.myquranapp.model.Data
import com.disc.myquranapp.model.QuranVerse
import com.disc.myquranapp.setData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class SurahActivity : AppCompatActivity() {

    lateinit var binding : ActivitySurahBinding
    lateinit var surahDetails : Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor()
        hideNavigationBar(this)
        val ayatList = mutableListOf<QuranVerse>()
        surahDetails = intent.getParcelableExtra("surahDetails")!!

        val versesMap: Map<String, QuranVerse> = parseJsonToMap(R.raw.simplequran)

        binding.myTextView.text = surahDetails.name

        for ((key, value) in versesMap) {
            if (value.surah.toString() == surahDetails.number.toString()) {
                ayatList.add(value)
            }
        }

        binding.recyclerview.setData(ayatList, AyatItemviewBinding::inflate) { binding, item, position ->
            binding.ayat.text = item.verse
            binding.ayatNumber.text = item.ayah.toString()
        }

    }



    inline fun <reified T> parseJsonToMap(resourceId: Int): Map<String, T> {
        return try {
            val inputStream = resources.openRawResource(resourceId)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)

            val type = object : TypeToken<Map<String, T>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: IOException) {
            e.printStackTrace()
            emptyMap()
        }
    }
}