package com.disc.myquranapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.disc.myquranapp.MainActivity
import com.disc.myquranapp.R
import com.disc.myquranapp.activitys.QuranBySurahActivity
import com.disc.myquranapp.databinding.FragmentNamazBinding

class QuranFragment : Fragment() {

    lateinit var binding: FragmentNamazBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNamazBinding.inflate(layoutInflater, container, false)


        binding.card1.setOnClickListener {
            val intent = Intent(requireContext(), QuranBySurahActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

}