package com.disc.myquranapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.disc.myquranapp.R
import com.disc.myquranapp.activitys.qibla.Compass
import com.disc.myquranapp.activitys.qibla.GPSTracker
import com.disc.myquranapp.databinding.FragmentQiblaBinding
import com.disc.myquranapp.databinding.SurahListItemBinding
import java.io.IOException
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


class QiblaFragment : Fragment() {

    lateinit var binding: FragmentQiblaBinding
    private var currentAzimuth = 0f
    private var city: String? = null
    private lateinit var prefs: SharedPreferences
    var gps: GPSTracker? = null
    var geocoder: Geocoder? = null
    private var compass: Compass? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQiblaBinding.inflate(layoutInflater, container, false)

        prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        setupCompass()
        fetchGPS()

        val qiblaDeg = Math.round(GetFloat("qibla_degree")).toString() + "°"

        binding.textCity!!.text = city
        binding.textDegree!!.text = qiblaDeg

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        if (compass != null) {
            compass!!.start()
        }
    }

    override fun onStop() {
        super.onStop()

        if (compass != null) {
            compass!!.stop()
        }
    }

    override fun onPause() {
        super.onPause()

        if (compass != null) {
            compass!!.stop()
        }
    }


    override fun onResume() {
        super.onResume()

        if (compass != null) {
            compass!!.start()
        }
    }

    fun SaveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun SaveFloat(key: String?, value: Float?) {
        val editor = prefs!!.edit()
        editor.putFloat(key, value!!)
        editor.apply()
    }

    fun GetFloat(key: String?): Float {
        return prefs!!.getFloat(key, 0f)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setupCompass() {
        val permissionGranted = getBoolean("permission_granted")

        if (permissionGranted) {
            bearing
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
        }

        compass = Compass(requireContext())
        val compassListener = Compass.CompassListener { azimuth ->
            requireActivity().runOnUiThread {
                adjustCompassDegree(azimuth)
                adjustCompassArrow(azimuth)
            }
        }

        compass!!.setListener(compassListener)
    }

    fun adjustCompassDegree(azimuth: Float) {
        val animation: Animation = RotateAnimation(
            -currentAzimuth,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = (azimuth)
        animation.duration = 500
        animation.repeatCount = 0
        animation.fillAfter = true
        binding.compassDegree!!.animation = animation
    }

    fun adjustCompassArrow(azimuth: Float) {
        val qiblaDegree = GetFloat("qibla_degree")
        val animation: Animation = RotateAnimation(
            -(currentAzimuth) + qiblaDegree,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = (azimuth)

        val minAzimuth = (floor(GetFloat("qibla_degree").toDouble()) - 3).toInt()
        val maxAzimuth = (ceil(GetFloat("qibla_degree").toDouble()) + 3).toInt()

        if (currentAzimuth >= minAzimuth && currentAzimuth <= maxAzimuth) {
            binding.compassOuter!!.setImageResource(R.drawable.compass_outer_green)
        } else {
            binding.compassOuter!!.setImageResource(R.drawable.compass_outer_gray)
        }

        animation.duration = 500
        animation.repeatCount = 0
        animation.fillAfter = true
        binding.compassArrow!!.startAnimation(animation)
    }

    @get:SuppressLint("MissingPermission")
    val bearing: Unit
        get() {
            val qiblaDegree = GetFloat("qibla_degree")
            if (qiblaDegree <= 0.0001) {
                fetchGPS()
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SaveBoolean("permission_granted", true)
                setupCompass()
            } else {
                Toast.makeText(requireContext(), "Permission tidak diizinkan!", Toast.LENGTH_LONG)
                    .show()
//                finish()
            }
            return
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun fetchGPS() {
        val result: Double
        gps = GPSTracker(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        if (gps!!.canGetLocation()) {
            val myLat = gps!!.latitude
            val myLon = gps!!.longitude

            try {
                val addresses = geocoder!!.getFromLocation(myLat, myLon, 1)
                if (addresses != null) {
                    city = addresses[0].subAdminArea
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (myLat >= 0.001 && myLon >= 0.001) {
                // posisi ka'bah
                val kabahLat = Math.toRadians(21.422487)
                val kabahLon = 39.826206
                val myRadiansLat = Math.toRadians(myLat)
                val lonDiff = Math.toRadians(kabahLon - myLon)
                val y = sin(lonDiff) * cos(kabahLat)
                val x = cos(myRadiansLat) * sin(kabahLat) - sin(myRadiansLat) * cos(kabahLat) * cos(
                    lonDiff
                )
                result = (Math.toDegrees(atan2(y, x)) + 360) % 360
                SaveFloat("qibla_degree", result.toFloat())
            }
        } else {
            gps!!.showSettingAlert()
        }
    }
}