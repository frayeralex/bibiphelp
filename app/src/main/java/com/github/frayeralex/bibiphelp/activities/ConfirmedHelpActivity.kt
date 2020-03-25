package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.github.frayeralex.bibiphelp.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.App
import com.github.frayeralex.bibiphelp.constatns.EventStatuses
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.utils.EventModelUtils
import com.github.frayeralex.bibiphelp.utils.MapUtils
import com.github.frayeralex.bibiphelp.viewModels.ConfirmedHelpViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_confirmed_help.*


class ConfirmedHelpActivity : AppCompatActivity(), OnMapReadyCallback {

    private val app by lazy { application as App }
    private val viewModel by viewModels<ConfirmedHelpViewModel>()
    lateinit var eventId: String
    var event: EventModel? = null
    private lateinit var mMap: GoogleMap
    private var myLocationMarker: Marker? = null
    private var eventMarker: Marker? = null
    private var challenger: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_help)

        eventId = intent.getStringExtra(IntentExtra.eventId)!!

        challenger = intent.getIntExtra(IntentExtra.challenger, 0)




        app.getCacheManager().meActiveHelperForEvent = eventId

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.confirmedHelpMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        rejectBtn.setOnClickListener { handleRejectBtnClick() }
    }

    override fun onBackPressed() {}

    private fun handleRejectBtnClick() {
        app.getCacheManager().resetMeHelpForEvent()
        val intent = Intent(this, RejectHelpActivity::class.java)

        intent.putExtra(IntentExtra.eventId, this.event?.id)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun updateUI(event: EventModel?) {
        this.event = event
        if (event != null && checkEventStatus(event)) {
            if (eventMarker == null) {
                eventMarker = mMap.addMarker(EventModelUtils.getMapMarker(event))
            }
            updateMapCamera()

            updateHelpCounterUi(event)
        }
    }

    private fun updateHelpCounterUi(event: EventModel) {
        val currentHelpersCount = (helpersCount.text as String).toInt()
        val updatedHelpersCount = event.helpers.size
        helpersCount.text = updatedHelpersCount.toString()

        if ((updatedHelpersCount > currentHelpersCount) and (challenger == 1)) {
            Toast.makeText(
                baseContext, R.string.confirmed_help_main_label,
                Toast.LENGTH_LONG
            ).show()
        }
        else {
            Toast.makeText(
                baseContext, R.string.confirmed_help_more_helpers_count,
                Toast.LENGTH_LONG
            ).show()
        }

        if (updatedHelpersCount < currentHelpersCount) {
            Toast.makeText(
                baseContext, R.string.confirmed_help_less_helpers_count,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkEventStatus(event: EventModel): Boolean {
        if (event.status != EventStatuses.ACTIVE) {
            Toast.makeText(
                baseContext, R.string.confirmed_help_event_closed,
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            return false
        }
        return true
    }

    private fun updateMapCamera() {
        val markers = mutableListOf<LatLng>()

        if (eventMarker != null) {
            markers.add(LatLng(
                eventMarker?.position?.latitude!!,
                eventMarker?.position?.longitude!!
            ))
        }

        if (myLocationMarker != null) {
            markers.add(LatLng(
                myLocationMarker?.position?.latitude!!,
                myLocationMarker?.position?.longitude!!
            ))
        }

        MapUtils.updateMapCamera(mMap, markers)
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

        MapUtils.updateStyle(this, mMap)

        viewModel.getEvent(eventId)
            .observe(this, Observer { updateUI(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> { updateMyLocationMarker(it) })
    }
}