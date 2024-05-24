package com.disc.myquranapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.disc.myquranapp.databinding.ActivityMainBinding
import com.disc.myquranapp.databinding.SurahListItemBinding
import com.disc.myquranapp.fragments.HadisFragment
import com.disc.myquranapp.fragments.NamazFragment
import com.disc.myquranapp.fragments.QiblaFragment
import com.disc.myquranapp.fragments.QuranFragment
import com.disc.myquranapp.model.Data
import com.disc.myquranapp.model.QuranVerse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.statusBarColor(this)

        if (savedInstanceState == null) {
            loadFragment(QuranFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_quran -> {
                    loadFragment(QuranFragment())
                    true
                }
                R.id.tab_hadis -> {
                    loadFragment(HadisFragment())
                    true
                }
                R.id.tab_namaz -> {
                    loadFragment(NamazFragment())
                    true
                }
                R.id.tab_qibla -> {
                    loadFragment(QiblaFragment())
                    true
                }
                else -> false
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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