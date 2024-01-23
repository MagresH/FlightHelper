package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var planeImageView: ImageView
    private lateinit var translateAnimation: TranslateAnimation
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        setContentView(R.layout.activity_login)
        playSound()
        val btn = findViewById<Button>(R.id.fingerprintButton)
        val btn2 = findViewById<Button>(R.id.pinCodeButton)
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()
        planeImageView = findViewById<ImageView>(R.id.imageView2)
        planeImageView.x = -800f
        planeImageView.y = screenHeight - planeImageView.height.toFloat() - 400f

        val landX = screenWidth / 1.4f - planeImageView.width.toFloat()
        val landY = -(screenHeight / 1.3f - planeImageView.height.toFloat())

        translateAnimation = TranslateAnimation(
            0f, landX, 0f, landY
        )
        translateAnimation.duration = 3000
        translateAnimation.fillAfter = true

        val executer = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executer,

            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    Toast.makeText(
                        applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed", Toast.LENGTH_SHORT
                    ).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel")
            .build()


        btn.setOnClickListener() {
            vibrate()

            val newTranslateAnimation = TranslateAnimation(
                landX, screenWidth * 2, landY, -screenHeight * 2
            )

            newTranslateAnimation.duration = 1000 //
            translateAnimation.fillAfter = true
            planeImageView.startAnimation(newTranslateAnimation)
            newTranslateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    biometricPrompt.authenticate(promptInfo)

                }

                override fun onAnimationRepeat(animation: Animation?) {
                    planeImageView.startAnimation(translateAnimation)

                }

            })

        }

        btn2.setOnClickListener {
            vibrate()

            val newTranslateAnimation = TranslateAnimation(
                landX, screenWidth * 2, landY, -screenHeight * 2
            )

            newTranslateAnimation.duration = 1000
            translateAnimation.fillAfter = true
            planeImageView.startAnimation(newTranslateAnimation)
            newTranslateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    biometricPrompt.authenticate(
                        BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Login with your default security method")
                            .setAllowedAuthenticators(DEVICE_CREDENTIAL).build()
                    )
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    planeImageView.startAnimation(translateAnimation)

                }

            })


        }


    }
    private fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.landing)
        }
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            stopSound()
        }
    }

    private fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    override fun onResume() {
        playSound()
        super.onResume()
        planeImageView.startAnimation(translateAnimation)

    }
    override fun onPause() {
        stopSound()
        super.onPause()
    }
    private fun vibrate() {
        vibrator?.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    override fun onDestroy() {
        stopSound()
        super.onDestroy()
    }
}