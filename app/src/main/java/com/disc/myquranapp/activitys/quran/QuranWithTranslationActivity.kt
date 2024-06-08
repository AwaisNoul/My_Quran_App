package com.disc.myquranapp.activitys.quran

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.model.QuranWithTranslationItem
import com.disc.myquranapp.R
import com.disc.myquranapp.model.Verse
import com.disc.myquranapp.databinding.ActivityQuranWithTranslationBinding
import com.disc.myquranapp.databinding.ItemviewSurahListWithTranslationBinding
import com.disc.myquranapp.setData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class QuranWithTranslationActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuranWithTranslationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuranWithTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor()

        val surahList: List<QuranWithTranslationItem> = parseJsonToList(R.raw.quran_with_translation)
        binding.recyclerview.setData(surahList, ItemviewSurahListWithTranslationBinding::inflate) { b, item, position ->
            b.id.text = item.id.toString()
            b.transliteration.text = item.translation
            b.translation.text = item.transliteration
            b.type.text = item.type

            b.root.setOnClickListener {
                val intent = Intent(this@QuranWithTranslationActivity, QuranWithTranslationVerseActivity::class.java)
                intent.putParcelableArrayListExtra("verse_list", item.verses as ArrayList<Verse>)
                startActivity(intent)
            }
        }



        binding.backArrow.setOnClickListener {
            finish()
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