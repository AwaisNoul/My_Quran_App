package com.disc.myquranapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.disc.myquranapp.MyExtensions.statusBarColor
import com.disc.myquranapp.databinding.ActivityMainBinding
import com.disc.myquranapp.fragments.HadisFragment
import com.disc.myquranapp.fragments.NamazFragment
import com.disc.myquranapp.fragments.QiblaFragment
import com.disc.myquranapp.fragments.QuranFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor()
        Utils.systemNavigationColor(this)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, QuranFragment()).commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it) {
                0 -> replaceFragment(QuranFragment())
                1 -> replaceFragment(HadisFragment())
                2 -> replaceFragment(NamazFragment())
                3 -> replaceFragment(QiblaFragment())
            }
        }

    }


    @SuppressLint("MissingInflatedId")
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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