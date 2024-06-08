package com.disc.myquranapp.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.disc.myquranapp.R
import com.disc.myquranapp.activitys.qibla.GPSTracker
import com.disc.myquranapp.databinding.FragmentNamazBinding
import org.json.JSONException
import java.time.LocalDate

class NamazFragment : Fragment() {

    lateinit var binding: FragmentNamazBinding
    var gps: GPSTracker? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNamazBinding.inflate(layoutInflater,container,false)


        val date = LocalDate.now()
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth

        gps = GPSTracker(requireContext())
        if (gps!!.canGetLocation()) {
            val lat = gps!!.latitude
            val lon = gps!!.longitude

            val requestQueue = Volley.newRequestQueue(requireContext())
            val url = String.format(
                "https://api.aladhan.com/v1/calendar/%d/%d?latitude=%f&longitude=%f&method=99",
                year,
                month,
                lat,
                lon
            )
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    try {
                        val jsonArray = response.getJSONArray("data")
                        val jsonObject = jsonArray.getJSONObject(day - 1)
                        val data = jsonObject.getJSONObject("timings")

                        binding.textSubuh.text = data.getString("Fajr")
                        binding.textZuhur.text = data.getString("Dhuhr")
                        binding.textAshar.text = data.getString("Asr")
                        binding.textMaghrib.text = data.getString("Maghrib")
                        binding.textIsya.text = data.getString("Isha")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                })

            requestQueue.add(jsonObjectRequest)
        }


        return binding.root
    }

}