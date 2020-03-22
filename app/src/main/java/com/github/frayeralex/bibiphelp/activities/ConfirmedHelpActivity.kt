package com.github.frayeralex.bibiphelp.activities

import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.frayeralex.bibiphelp.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.utils.EventModelUtils
import com.github.frayeralex.bibiphelp.viewModels.ConfirmedHelpViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class ConfirmedHelpActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by viewModels<ConfirmedHelpViewModel>()
    lateinit var eventId: String
    private lateinit var mMap: GoogleMap
    private var myLocationMarker: Marker? = null
    private var eventMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_help)

        eventId = intent.getStringExtra(IntentExtra.eventId)!!

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.confirmedHelpMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onBackPressed() {}

    private fun updateUI(event: EventModel?) {
        if (event != null) {
            if (eventMarker == null) {
                eventMarker = mMap.addMarker(EventModelUtils.getMapMarker(event))
            }
            updateMapCamera()
        }
    }

    private fun updateMapCamera() {
        val builder = LatLngBounds.builder()

        if (eventMarker != null) {
            builder.include(LatLng(
                eventMarker?.position?.latitude!!,
                eventMarker?.position?.longitude!!
            ))
        }

        if (myLocationMarker != null) {
            builder.include(
                LatLng(
                    myLocationMarker?.position?.latitude!!,
                    myLocationMarker?.position?.longitude!!
                ))
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
    }

    private fun updateMyLocationMarker(location: Location?) {
        if (location != null) {
            if (myLocationMarker != null) {
                myLocationMarker?.remove()
                myLocationMarker = null
            }

            myLocationMarker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(location.latitude, location.longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_geolocation))
            )

            updateMapCamera()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true

        updateMapStyle()

        viewModel.getEvent(eventId)
            .observe(this, Observer { updateUI(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> { updateMyLocationMarker(it) })
    }

    private fun updateMapStyle() {
        try {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_styles
                )
            )
        } catch (e: Resources.NotFoundException) {
        }
    }
}