package com.disc.myquranapp.activitys.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class Compass(context: Context) : SensorEventListener {
    fun interface CompassListener {
        fun onNewAzimuth(azimuth: Float)
    }

    private var listener: CompassListener? = null

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val mGravity = FloatArray(3)
    private val mMagnetic = FloatArray(3)
    private val R = FloatArray(9)
    private val I = FloatArray(9)

    private var azimuthFix = 0f

    fun start() {
        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun setAzimuthFix(azimuth: Float) {
        azimuthFix = azimuth
    }

    fun setListener(listener: CompassListener?) {
        this.listener = listener
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f

        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMagnetic[0] = alpha * mMagnetic[0] + (1 - alpha) * event.values[0]
                mMagnetic[1] = alpha * mMagnetic[1] + (1 - alpha) * event.values[1]
                mMagnetic[2] = alpha * mMagnetic[2] + (1 - alpha) * event.values[2]
            }

            val success = SensorManager.getRotationMatrix(R, I, mGravity, mMagnetic)
            if (success) {
                val orientation = FloatArray(3)

                SensorManager.getOrientation(R, orientation)

                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + azimuthFix + 360) % 360

                if (listener != null) {
                    listener!!.onNewAzimuth(azimuth)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
