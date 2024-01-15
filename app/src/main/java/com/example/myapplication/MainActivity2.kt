package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphFloatingActionButton


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        val airplaneModeToggleButton = findViewById<NeumorphFloatingActionButton>(R.id.airplaneToggleButton)

        airplaneModeToggleButton.setOnClickListener() {
            val shape = if (airplaneModeToggleButton.getShapeType() == 0) 1 else 0
            airplaneModeToggleButton.setShapeType(shape)
        }


    }
}