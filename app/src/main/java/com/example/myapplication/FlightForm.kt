package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class FlightForm : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase

    private lateinit var autoCompleteTextViewFrom: AutoCompleteTextView
    private lateinit var autoCompleteTextViewTo: AutoCompleteTextView
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var imageViewBoardingPass: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var btnAddUpdateFlight: Button

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_form)
        database = FirebaseDatabase.getInstance()
        autoCompleteTextViewFrom = findViewById(R.id.autoCompleteTextViewFrom)
        autoCompleteTextViewTo = findViewById(R.id.autoCompleteTextViewTo)
        editTextDate = findViewById(R.id.editTextDate)
        editTextTime = findViewById(R.id.editTextTime)
        imageViewBoardingPass = findViewById(R.id.imageViewBoardingPass)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnAddUpdateFlight = findViewById(R.id.btnAddUpdateFlight)

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        editTextTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnPickImage.setOnClickListener {
            openGallery()
        }

        btnAddUpdateFlight.setOnClickListener {
            val from = autoCompleteTextViewFrom.text.toString()
            val to = autoCompleteTextViewTo.text.toString()
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val flightId = database.getReference("flights").push().key
            var imageFileName = ""
            val storageReference = FirebaseStorage.getInstance().reference

            if (from.isEmpty() || to.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedImageUri != null) {
                imageFileName = "$flightId.jpg"

            }
            val flight = Flight(
                id = flightId ?: "",
                from = from,
                to = to,
                departureDate = date,
                departureTime = time,
                imageUrl = imageFileName
            )

            database.getReference("flights").child(flightId ?: "").setValue(flight).addOnSuccessListener {
                val selectedImageUri = selectedImageUri
                selectedImageUri?.let {
                    storageReference.child("flight_images/$imageFileName").putFile(it)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Problem with saving picture: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                Toast.makeText(this, "Flight added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FlightListActivity::class.java)
                startActivity(intent)
            }

        }

    }

    fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editTextDate.setText(dateFormat.format(calendar.time))
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    fun showTimePickerDialog() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            editTextTime.setText(timeFormat.format(calendar.time))
        }

        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imageViewBoardingPass.setImageURI(selectedImageUri)
    }

}
}


