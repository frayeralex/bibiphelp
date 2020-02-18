package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.frayeralex.bibiphelp.list_users.SingltonUser
import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var eventsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomBar: LinearLayout
    private lateinit var askHelpBtn: Button

    private val markerMap: MutableMap<String, Marker> = mutableMapOf()
    private var myLocationMarker: Marker? = null

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        Toast.makeText(
                            baseContext, user?.uid.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_activity, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        askHelpBtn = findViewById(R.id.button_help)
        askHelpBtn.setOnClickListener { handleAskHelpBtnClick(it) }

        bottomBar = findViewById(R.id.bottomBar)
        bottomBar.setOnClickListener { handleCloseBtnClick(it) }

        bottomBar.post { bottomBar.translationY = bottomBar.height.toFloat() }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        eventsRef = FirebaseDatabase.getInstance().getReference(DB_EVENTS)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_list_view -> {
            //todo: implement intent to ListViewActivity
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleCloseBtnClick(view: View) {
        fadeOut(bottomBar)
    }

    private fun handleAskHelpBtnClick(view: View) {
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivity(intent)
    }

    private fun fadeOut(view: View) {
        view.animate()
            .setDuration(DEFAULT_ANIMATION_DURATION.toLong())
            .translationY(view.height.toFloat())
            .setListener(null)
    }

    private fun fadeIn(view: View) {
        view.animate()
            .setDuration(DEFAULT_ANIMATION_DURATION.toLong())
            .translationY(0f)
            .setListener(null)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // todo handle
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_FINE_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { addMyLocationMarker(it) }
                } else {
                    // todo
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> { }
        }
    }

    private fun addMyLocationMarker(location: Location?) {
        if (location != null) {
            if (myLocationMarker == null) {
                myLocationMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(location.latitude, location.longitude))
                        .anchor((0.5).toFloat(), (0.5).toFloat())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_geolocation))
                )

                mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.setOnMarkerClickListener(this)

        updateMapStyle()
        listenEventChanges()
        checkLocationPermission()

        fusedLocationClient.lastLocation.addOnSuccessListener { addMyLocationMarker(it) }
    }

    private fun listenEventChanges() {
        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                handleEventChanged(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                handleEventChangeError(error)
            }
        })
    }

    private fun handleEventChanged(dataSnapshot: DataSnapshot) {
        for (eventSnapshot in dataSnapshot.children) {
            updateEventMarkers(eventSnapshot.getValue(EventModel::class.java))
        }
    }

    private fun updateEventMarkers(event: EventModel?) {
        if (event is EventModel && event.id != "") {
            val marker = markerMap[event.id]

            if (marker is Marker) {
                marker.title = event.message
                marker.position = LatLng(event.lat!!, event.long!!)

                reDrawMarker(marker)
            } else {
                val newMarker = mMap.addMarker(event.getMapMarker())
                newMarker.tag = event.id
                markerMap.put(event.id!!, newMarker)
            }
        }
    }

    private fun handleEventChangeError(error: DatabaseError) {
        Log.w(TAG, "Failed to read value.", error.toException())
    }

    private fun reDrawMarker(marker: Marker) {
        val isInfoWindowShown = marker.isInfoWindowShown

        marker.isVisible = false
        marker.isVisible = true

        if (isInfoWindowShown) {
            marker.showInfoWindow()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val eventId = marker?.tag as String?

        if (eventId != null) {
            val label = findViewById<TextView>(R.id.bottomTypeLabel)
            label.text = marker!!.title

            if (bottomBar.translationY != 0f) {
                fadeIn(bottomBar)
            }
        }

        return false
    }


    private fun updateMapStyle() {
        try {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_styles
                )
            )
        } catch (e: NotFoundException) {
        }
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY"
        const val DB_EVENTS = "events"
        const val ACCESS_FINE_LOCATION = 1
        const val DEFAULT_ANIMATION_DURATION = 300
    }
}
