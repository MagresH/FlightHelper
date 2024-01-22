package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton

class FlightListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseAdapter: FirebaseFlightAdapter
    private lateinit var addFlightButton: NeumorphButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        recyclerView = findViewById(R.id.recyclerViewFlights)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addFlightButton = findViewById(R.id.addFlightBtn)

        val query = FirebaseDatabase.getInstance().reference.child("flights")
        val options = FirebaseRecyclerOptions.Builder<Flight>()
            .setQuery(query, Flight::class.java)
            .build()

        firebaseAdapter = FirebaseFlightAdapter(options) { clickedFlight ->
            val intent = FlightDetailsActivity.newIntent(this, clickedFlight.id)
            startActivity(intent)
        }
        addFlightButton.setOnClickListener() {
            val intent = Intent(applicationContext, FlightForm::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = firebaseAdapter

    }
    override fun onResume() {
        super.onResume()
        firebaseAdapter.notifyDataSetChanged()
    }
    override fun onStart() {
        super.onStart()
        firebaseAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        firebaseAdapter.stopListening()
    }
}
