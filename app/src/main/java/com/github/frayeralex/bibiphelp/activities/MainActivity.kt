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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.models.EventModelUtils
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
import com.github.frayeralex.bibiphelp.list_users.ListEvent
import com.github.frayeralex.bibiphelp.utils.DistanceCalculator
import com.github.frayeralex.bibiphelp.utils.MapUtils
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
    private var myLocationMarker: Marker? = null

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
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        updateMyLocationMarker(
                            it
                        )
                    }
                } else {
                    // todo
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
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

    private fun updateMyLocationMarker(location: Location?) {
        if (location != null) {
            if (myLocationMarker != null) {
                myLocationMarker?.remove()
                myLocationMarker = null
            }

            myLocationMarker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(location.latitude, location.longitude))
                    .anchor((0.5).toFloat(), (0.5).toFloat())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_geolocation))
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false

        mMap.setOnMarkerClickListener(this)
        mMap.setOnCameraIdleListener(this)

        MapUtils.updateStyle(this, mMap)
        updateCamera()
        checkLocationPermission()

        viewModel.getEvents()
            .observe(this, Observer<MutableList<EventModel>> { handleEventsUpdated(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> { updateMyLocationMarker(it) })
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
            if (myLocationMarker != null) {
                val distance = DistanceCalculator.distance(
                    marker.position.latitude,
                    marker.position.longitude,
                    myLocationMarker?.position?.latitude!!,
                    myLocationMarker?.position?.longitude!!
                )

                this.distance = distance?: 0.0

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
        const val ACCESS_FINE_LOCATION = 1
        const val DEFAULT_ANIMATION_DURATION = 300
    }
}
