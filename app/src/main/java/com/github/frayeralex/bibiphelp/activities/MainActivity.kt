package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.App
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.utils.DistanceCalculator
import com.github.frayeralex.bibiphelp.utils.EventModelUtils
import com.github.frayeralex.bibiphelp.utils.MapUtils
import com.github.frayeralex.bibiphelp.utils.PermissionManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnCameraIdleListener {

    private val app by lazy { application as App }
    private val viewModel by viewModels<ListEventViewModel>()
    private lateinit var mMap: GoogleMap
    private var user: FirebaseUser? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var selectedEventId: String? = null
    private val markerMap: MutableMap<String, Marker> = mutableMapOf()
    private var myLocation: Location? = null

    private var distance: Double? = null

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

        askHelpBtn.setOnClickListener { handleAskHelpBtnClick(it) }

        bottomBar.setOnClickListener { handleCloseBtnClick(it) }

        bottomBar.post { bottomBar.translationY = bottomBar.height.toFloat() }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.getUser().observe(this, Observer<FirebaseUser> { user = it })

        viewModel.getEventsRequestStatus()
            .observe(this, Observer<String> { handleEventRequestStatus(it) })

        bottomBtn.setOnClickListener { handleHelpBtnClick() }
    }

    private fun handleHelpBtnClick() {
        if (selectedEventId != null) {
            val intent = Intent(this, EventDetails::class.java)
            intent.putExtra(IntentExtra.eventId, selectedEventId)
            intent.putExtra(IntentExtra.eventDistance, distance)
            startActivity(intent)
        }
    }

    private fun handleEventRequestStatus(status: String) {
        when (status) {
            RequestStatuses.PENDING -> {
                progressBar.isVisible = true
            }
            RequestStatuses.SUCCESS -> {
                progressBar.isVisible = false
            }
            RequestStatuses.FAILURE -> {
                progressBar.isVisible = false
                Toast.makeText(
                    baseContext, R.string.error_common,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleEventsUpdated(events: MutableList<EventModel>?) {
        markerMap.forEach { it.value.remove() }
        markerMap.clear()


        events?.filter { event -> event.userId != user?.uid }?.forEach { updateEventMarkers(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_list_view -> {
            val intent = Intent(this, ListEventActivity::class.java)
            startActivity(intent)
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
        PermissionManager.checkLocationPermission(this, ACCESS_FINE_LOCATION)
    }

    private fun showMyLocationBtn() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap?.isMyLocationEnabled = true
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
                    showMyLocationBtn()
                    fusedLocationClient.lastLocation.addOnSuccessListener { myLocation = it }
                }
                return
            }
            else -> {
            }
        }
    }

    private fun updateCamera() {
        try {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        app.getCacheManager().lastMapLat.toDouble(),
                        app.getCacheManager().lastMapLong.toDouble()
                    )
                )
            )
        } catch (e: NotFoundException) {
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setPadding(20, 20, 20, 150)

        mMap.setOnMarkerClickListener(this)
        mMap.setOnCameraIdleListener(this)

        MapUtils.updateStyle(this, mMap)
        updateCamera()
        checkLocationPermission()

        viewModel.getEvents()
            .observe(this, Observer<MutableList<EventModel>> { handleEventsUpdated(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> {
                myLocation = it
            })
        showMyLocationBtn()
    }

    private fun updateEventMarkers(event: EventModel?) {
        if (event is EventModel && event.id != "") {
            val newMarker = mMap.addMarker(EventModelUtils.getMapMarker(event))
            newMarker.tag = event.id
            markerMap.put(event.id!!, newMarker)
        }
    }

    override fun onCameraIdle() {
        persistMapPosition()
    }

    private fun persistMapPosition() {
        app.getCacheManager().lastMapLat = mMap.cameraPosition.target.latitude.toFloat()
        app.getCacheManager().lastMapLong = mMap.cameraPosition.target.longitude.toFloat()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        selectedEventId = marker?.tag as String?

        if (selectedEventId != null) {
            bottomTypeLabel.text = marker!!.title
            if (myLocation != null) {
                val distance = DistanceCalculator.distance(
                    marker.position.latitude,
                    marker.position.longitude,
                    myLocation?.latitude!!,
                    myLocation?.longitude!!
                )

                this.distance = distance ?: 0.0

                distanceLabel.text = resources.getString(
                    R.string.distance_km!!,
                    DistanceCalculator.formatDistance(distance)
                )
            }

            if (bottomBar.translationY != 0f) {
                fadeIn(bottomBar)
            }
        }

        return false
    }

    companion object {
        const val ACCESS_FINE_LOCATION = 1
        const val DEFAULT_ANIMATION_DURATION = 300
    }
}
