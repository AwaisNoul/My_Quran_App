package com.disc.myquranapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.disc.myquranapp.activitys.quran.QuranBySurahActivity
import com.disc.myquranapp.activitys.quran.QuranWithTranslationActivity
import com.disc.myquranapp.databinding.FragmentNamazBinding
import com.disc.myquranapp.databinding.FragmentQuranBinding

class QuranFragment : Fragment() {

    lateinit var binding: FragmentQuranBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuranBinding.inflate(layoutInflater, container, false)


        binding.card1.setOnClickListener {
            val intent = Intent(requireContext(), QuranBySurahActivity::class.java)
            startActivity(intent)
        }

        binding.withTranslation.setOnClickListener {
            val intent = Intent(requireContext(), QuranWithTranslationActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

}