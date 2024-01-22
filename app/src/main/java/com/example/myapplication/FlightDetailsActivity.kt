package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.Flight
import com.example.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class FlightDetailsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)


        database = FirebaseDatabase.getInstance().reference.child("flights")
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference.child("flight_images")

        val flightId = intent.getStringExtra("id")

        database.child(flightId ?: "").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val flight = dataSnapshot.getValue(Flight::class.java)

                findViewById<TextView>(R.id.textViewFromTo).text =
                    "${flight?.from} -> ${flight?.to}"
                findViewById<TextView>(R.id.textViewDateTime).text =
                    "Date and Time\n ${flight?.departureDate} ${flight?.departureTime}"

                storageRef.child(flight?.imageUrl ?: "").downloadUrl.addOnSuccessListener {
                    Glide.with(this)
                        .load(it)
                        .into(findViewById<ImageView>(R.id.imageViewDetail))
                    findViewById<ImageView>(R.id.imageViewDetail).apply {
                        tag = it.toString() // Ustaw tag na URL obrazu
                        setOnClickListener { view ->
                            openSystemImageGallery(view.tag?.toString() ?: "")
                        }
                }

                findViewById<Button>(R.id.btnDeleteFlight).setOnClickListener {
                    deleteFlight(flightId ?: "")
                }}
            }
        }
    }

    private fun deleteFlight(flightId: String) {
        database.child(flightId).removeValue()
        finish()
    }
        private fun openSystemImageGallery(imageUrl: String) {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(imageUrl)
            }
            startActivity(intent)
        }
    companion object {
        fun newIntent(context: AppCompatActivity, flightId: String): Intent {
            val intent = Intent(context, FlightDetailsActivity::class.java)
            intent.putExtra("id", flightId)
            return intent
        }
    }
}
