package com.disc.myquranapp.activitys.quran

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.R
import com.disc.myquranapp.databinding.ActivityQuranBySurahBinding
import com.disc.myquranapp.databinding.SurahListItemBinding
import com.disc.myquranapp.model.Data
import com.disc.myquranapp.model.QuranVerse
import com.disc.myquranapp.setData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class QuranBySurahActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuranBySurahBinding
    var surahArrayList = emptyList<Data>()
    var versesList = emptyList<QuranVerse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuranBySurahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val surahList: List<Data> = parseJsonToList(R.raw.quran)
        binding.recyclerview.setData(
            surahList,
            SurahListItemBinding::inflate
        ) { binding, item, position ->
            binding.arabicName.text = item.name
            binding.englishName.text = item.englishName
            binding.ayatNumber.text = item.number.toString()
            binding.revelationType.text = item.revelationType
            binding.root.setOnClickListener {
                val intent = Intent(this, SurahActivity::class.java)
                intent.putExtra("surahDetails", item)
                startActivity(intent)
            }
        }

    }


    private inline fun <reified T> parseJsonToList(resourceId: Int): List<T> {
        return try {
            val inputStream = resources.openRawResource(resourceId)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)

            val type = object : TypeToken<List<T>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}