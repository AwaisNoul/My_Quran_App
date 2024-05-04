package com.disc.myquranapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.disc.myquranapp.databinding.ActivityMainBinding
import com.disc.myquranapp.databinding.SurahListItemBinding
import com.disc.myquranapp.model.Data
import com.disc.myquranapp.model.QuranVerse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
//    var surahArrayList = emptyList<Data>()
    var versesList = emptyList<QuranVerse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.navy)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val surahList: List<Data> = parseJsonToList(R.raw.quran)
        binding.recyclerview.setData(surahList, SurahListItemBinding::inflate) { binding, item, position ->
            binding.arabicName.text = item.name
            binding.englishName.text = item.englishName
            binding.ayatNumber.text = item.number.toString()
            binding.revelationType.text = item.revelationType
            binding.root.setOnClickListener {
                val intent = Intent(this,SurahActivity::class.java)
                intent.putExtra("surahDetails",item)
                startActivity(intent)
            }
        }

    }

    inline fun <reified T> parseJsonToList(resourceId: Int): List<T> {
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



















inline fun <T, VB : ViewBinding> RecyclerView.setData(
    items: List<T>,
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    crossinline bindHolder: (binding: VB, item: T, position: Int) -> Unit
) {
    val adapter = object : RecyclerView.Adapter<DataViewHolder<VB>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<VB> {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = bindingInflater(layoutInflater, parent, false)
            return DataViewHolder(binding)
        }
        override fun onBindViewHolder(holder: DataViewHolder<VB>, position: Int) {
            bindHolder(holder.binding, items[position], position)
        }
        override fun getItemCount(): Int {
            return  items.size
        }
    }
    this.adapter = adapter
}
class DataViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)