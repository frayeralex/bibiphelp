package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.frayeralex.bibiphelp.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.utils.EventModelUtils
import com.github.frayeralex.bibiphelp.utils.DistanceCalculator
import com.github.frayeralex.bibiphelp.utils.MapUtils
import com.github.frayeralex.bibiphelp.viewModels.DetailsEventViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_details.*

class EventDetails : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by viewModels<DetailsEventViewModel>()
    lateinit var eventId: String
    var mDistanse: Double = 0.0
    private lateinit var mMap: GoogleMap
    private var eventMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbarDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventId = intent.getStringExtra(IntentExtra.eventId)!!
        mDistanse = intent.getDoubleExtra(IntentExtra.eventDistance, 0.0)

        distance.text = resources.getString(
            R.string.distance_km!!,
            DistanceCalculator.formatDistance(mDistanse)
        )

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapDetails) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getCategory().observe(this, Observer { updateCategory(it) })
        helpBtn.setOnClickListener { handleHelpBtnClick() }
    }

    private fun handleHelpBtnClick() {
        val intent = Intent(this, ConfirmedHelpActivity::class.java)
        intent.putExtra(IntentExtra.eventId, eventId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun updateUI(event: EventModel?) {
        if (event != null) {
            eventMsg.text = event.message
            if (eventMarker == null) {
                eventMarker = mMap.addMarker(EventModelUtils.getMapMarker(event))
            }
            updateMapCamera()
        }
    }

    private fun updateMapCamera() {
        if (eventMarker != null) {
            MapUtils.updateMapCamera(mMap, listOf(LatLng(
                eventMarker?.position?.latitude!!,
                eventMarker?.position?.longitude!!
            )), 14f)
        }
    }

    private fun updateCategory(category: EventCategoryModel?) {
        if (category != null) {
            eventCategoryLabel.text = category.label
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
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = false
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false

        MapUtils.updateStyle(this, mMap)

        viewModel.getEvent(eventId).observe(this, Observer { updateUI(it) })
    }
}