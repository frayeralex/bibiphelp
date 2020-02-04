package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var eventsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success")
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        eventsRef = FirebaseDatabase.getInstance().getReference(DB_EVENTS)

        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mMap.clear()
                for (eventSnapshot in dataSnapshot.children) {
                    val event = eventSnapshot.getValue(EventModel::class.java)
                    if(event != null) {
                        val marker = LatLng(event.lat!!, event.long!!)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(marker)
                                .title(event.message)
                                .icon(getPin(event))
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun getPin(event: EventModel) = when (event.type) {
        "type_1" -> BitmapDescriptorFactory.fromResource(R.drawable.pin_1)
        "type_2" -> BitmapDescriptorFactory.fromResource(R.drawable.pin_2)
        "type_3" -> BitmapDescriptorFactory.fromResource(R.drawable.pin_3)
        "type_4" -> BitmapDescriptorFactory.fromResource(R.drawable.pin_4)
        "type_5" -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
        else -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateMapStyle()
    }

    private fun updateMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_styles
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY"
        const val DB_EVENTS = "events"
    }
}
