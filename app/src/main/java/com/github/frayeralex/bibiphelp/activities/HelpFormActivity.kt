package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HelpFormActivity : AppCompatActivity() {

    private lateinit var eventsRef: DatabaseReference
    private var categoryId: Int? = null
    private lateinit var messageInput: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = intent.getIntExtra(CategoriesActivity.CATEGORY_ID_KEY, 6)
        setContentView(R.layout.activity_help_form)
        setSupportActionBar(findViewById(R.id.toolbar_help))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        messageInput = findViewById(R.id.message)

        eventsRef = FirebaseDatabase.getInstance().getReference(DB_EVENTS)
    }

    fun handlePublishBtnClick(view: View?) {
        val message = messageInput.text.toString().trim()

        if (message == "") {
            return Toast.makeText(
                baseContext, R.string.help_input_label_empty,
                Toast.LENGTH_SHORT
            ).show()
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            val eventRef = eventsRef.push()
            val eventData = EventModel(
                id = eventRef.key,
                message = message,
                type = categoryId,
                long = it.longitude,
                lat = it.latitude
            )

            val action = eventRef.setValue(eventData)

            action.addOnSuccessListener { successResult() }
            action.addOnCanceledListener { failureResult(it) }
        }
    }

    private fun successResult() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun failureResult(location: Location) {
        Log.d("Error", location.toString())
    }

    companion object {
        const val DB_EVENTS = "events"
    }
}