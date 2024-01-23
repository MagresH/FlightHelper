package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton


class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_ID"

    private var vibrator: Vibrator? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var planeImageView: ImageView
    private lateinit var translateAnimation: TranslateAnimation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.airplane)
            .setContentTitle("Flight helper")
            .setContentText("Zalogowano do aplikacji")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        setContentView(R.layout.activity_main)
        val flightDetailsButton = findViewById<NeumorphImageButton>(R.id.flightDetailsButton)
        val locationButton = findViewById<NeumorphImageButton>(R.id.locationButton)
        val airplaneModeToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton)
        val flashLightToggleButton = findViewById<NeumorphImageButton>(R.id.airplaneToggleButton2)
        val mainCardView = findViewById<NeumorphCardView>(R.id.card00)
        val wifiToggleButton = findViewById<NeumorphImageButton>(R.id.wifiToggleButton)
        var flashOn = false
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        planeImageView = findViewById<ImageView>(R.id.imageView3)
        planeImageView.x = -800f
        planeImageView.y = screenHeight - planeImageView.height.toFloat() - 500f

        val landX = screenWidth / 1.4f - planeImageView.width.toFloat()
        val landY = -(screenHeight / 1.3f - planeImageView.height.toFloat())

        translateAnimation = TranslateAnimation(
            0f, landX,
            0f, landY
        )
        translateAnimation.duration = 3000
        translateAnimation.fillAfter = true

        database = Firebase.database

        updateAirplaneModeToggleButton()
        updateWifiToggleButton()
        updateFlashLightToggleButton()

        locationButton.setOnClickListener() {
            vibrate()
            val newTranslateAnimation = TranslateAnimation(
                landX, screenWidth*2 ,
                landY, -screenHeight*2
            )

            newTranslateAnimation.duration = 1000 // Czas trwania animacji w milisekundach
            translateAnimation.fillAfter = true
            planeImageView.startAnimation(newTranslateAnimation)
            newTranslateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(applicationContext, MapsActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    planeImageView.startAnimation(translateAnimation)

                }

            })

        }

        flightDetailsButton.setOnClickListener() {
            vibrate()

            val newTranslateAnimation = TranslateAnimation(
                landX, screenWidth*2 ,
                landY, -screenHeight*2
            )

            newTranslateAnimation.duration = 1000 // Czas trwania animacji w milisekundach
            translateAnimation.fillAfter = true
            planeImageView.startAnimation(newTranslateAnimation)
            newTranslateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(applicationContext, FlightListActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    planeImageView.startAnimation(translateAnimation)

                }

            })

        }

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
        planeImageView.startAnimation(translateAnimation)

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
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "First channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Test description for my channel"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

}