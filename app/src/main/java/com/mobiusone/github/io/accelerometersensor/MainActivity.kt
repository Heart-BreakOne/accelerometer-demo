package com.mobiusone.github.io.accelerometersensor

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null

    private var oldX = 0.0
    private var oldY = 0.0
    private var oldZ = 0.0
    private var threadShould = 1000.0
    private var oldTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


    }

    override fun onSensorChanged(event: SensorEvent?) {
        val currentX = event!!.values[0]
        val currentY = event.values[1]
        val currentZ = event.values[2]
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - oldTime

        if (timeDiff > 100) {
            oldTime = currentTime
            val speed = kotlin.math.abs(currentX + currentY + currentZ - oldX - oldY - oldZ)/ timeDiff * 10000
            //val speed = Math.abs(currentX + currentY + currentZ - oldX - oldY - oldZ) / timeDiff * 10000

            if (speed > threadShould) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val v = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = v.defaultVibrator
                    vibrator.vibrate(500)
                    Toast.makeText(this, "Vibrate", Toast.LENGTH_SHORT).show()
                } else {
                    val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    v.vibrate(500)
                    Toast.makeText(this, "Vibrate", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }


    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)

    }


}
