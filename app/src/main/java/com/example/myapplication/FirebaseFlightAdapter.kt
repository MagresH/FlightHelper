package com.example.myapplication

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseFlightAdapter(
    options: FirebaseRecyclerOptions<Flight>,
    private val onItemClick: (Flight) -> Unit
) : FirebaseRecyclerAdapter<Flight, FirebaseFlightAdapter.FlightViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flight_card, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int, model: Flight) {
        holder.bind(model)

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(model)
            }
        }
    }

    class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewFromTo: TextView = itemView.findViewById(R.id.textViewFromTo)

        fun bind(flight: Flight) {
            val fromToText = "${flight.from} -> ${flight.to}"
            textViewFromTo.text = fromToText
            textViewFromTo.gravity = Gravity.CENTER
        }
    }
}
