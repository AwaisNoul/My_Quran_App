package com.disc.myquranapp.activitys.qibla

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class GPSTracker(private val context: Context) : Service(), LocationListener {
    private var isGPSEnabled: Boolean = false
    private var isNetworkEnabled: Boolean = false
    private var canGetLocation: Boolean = false
    private var location: Location? = null
    var lat: Double = 0.0
    var lon: Double = 0.0
    protected var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.e("Provider", "GPS dan Network provider tidak menyala")
            } else {
                this.canGetLocation = true

                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )

                    if (locationManager != null) {
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        if (location != null) {
                            lat = location!!.latitude
                            lon = location!!.longitude
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )

                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                            if (location != null) {
                                lat = location!!.latitude
                                lon = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    val latitude: Double
        get() {
            if (location != null) {
                lat = location!!.latitude
            }

            return lat
        }

    val longitude: Double
        get() {
            if (location != null) {
                lon = location!!.longitude
            }

            return lon
        }

    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    fun showSettingAlert() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.setTitle("Pengaturan GPS")
        alertDialog.setMessage("GPS tidak menyala. Pergi ke pengaturan?")
        alertDialog.setPositiveButton("Oke") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }

        alertDialog.setNegativeButton("Batal") { dialog, which -> dialog.cancel() }

        alertDialog.show()
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 100
        private const val MIN_TIME_BETWEEN_UPDATES = (1000 * 60).toLong()
    }
}
