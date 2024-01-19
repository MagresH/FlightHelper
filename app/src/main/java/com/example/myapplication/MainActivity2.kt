package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphFloatingActionButton
import soup.neumorphism.NeumorphImageButton


class MainActivity2 : AppCompatActivity() {
    private var vibrator: Vibrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        setContentView(R.layout.activity_main2)
        val airplaneModeToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton)
        val flashLightToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton2)
        val mainCardView = findViewById<NeumorphCardView>(R.id.card00)
        val wifiToggleButton = findViewById<NeumorphImageButton>(R.id.wifiToggleButton)
        var flashOn = false


        updateAirplaneModeToggleButton()
        updateWifiToggleButton()
        updateFlashLightToggleButton()

        airplaneModeToggleButton.setOnClickListener() {
            vibrate()
            changeAirplaneMode()
            if (isAirplaneModeOn()) {
                airplaneModeToggleButton.setShapeType(2)
                airplaneModeToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
            } else {
                airplaneModeToggleButton.setShapeType(0)
                airplaneModeToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
            }
        }
        mainCardView.setOnClickListener() {
            mainCardView.setShapeType(1)
        }


        flashLightToggleButton.setOnClickListener {
                vibrate()
                if (flashOn) {
                    flashLightOff()
                    flashOn = false
                    flashLightToggleButton.setShapeType(0)
                    flashLightToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
                } else {
                    flashLightOn()
                    flashOn = true
                    flashLightToggleButton.setShapeType(2)
                    flashLightToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
                }


        }

        wifiToggleButton.setOnClickListener {
            vibrate()
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
            if (isWifiOn()) {
                wifiToggleButton.setShapeType(2)
                wifiToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
            } else {
                wifiToggleButton.setShapeType(0)
                wifiToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
            }
        }

    }

    private fun updateFlashLightToggleButton() {
        val flashLightToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton2)
        if (isAirplaneModeOn()) {
            flashLightToggleButton.setShapeType(2)
            flashLightToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
        } else {
            flashLightToggleButton.setShapeType(0)
            flashLightToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
        }
    }

    private fun flashLightToggleButton() {
        val flashLightToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton2)
        if (isAirplaneModeOn()) {
            flashLightToggleButton.setShapeType(2)
            flashLightToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
        } else {
            flashLightToggleButton.setShapeType(0)
            flashLightToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
        }
    }

    private fun flashLightOff() {
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try {
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
        }
    }

    private fun flashLightOn() {
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try {
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: Exception) {
        }
    }

    private fun updateAirplaneModeToggleButton() {
        val airplaneModeToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton)
        if (isAirplaneModeOn()) {
            airplaneModeToggleButton.setShapeType(2)
            airplaneModeToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
        } else {
            airplaneModeToggleButton.setShapeType(0)
            airplaneModeToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
        }
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    private fun changeAirplaneMode() {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        updateAirplaneModeToggleButton()
        updateWifiToggleButton()
    }

    private fun updateWifiToggleButton() {
        val wifiToggleButton = findViewById<NeumorphImageButton>(R.id.wifiToggleButton)
        if (isWifiOn()) {
            wifiToggleButton.setShapeType(2)
            wifiToggleButton.setShadowColorDark(Color.parseColor("#D3ECCC"))
        } else {
            wifiToggleButton.setShapeType(0)
            wifiToggleButton.setShadowColorDark(Color.parseColor("#ECCCCC"))
        }
    }
    private fun vibrate() {
        vibrator?.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    private fun isWifiOn(): Boolean {
        return Settings.Global.getInt(
            contentResolver,
            Settings.Global.WIFI_ON,
            0
        ) != 0
    }
}