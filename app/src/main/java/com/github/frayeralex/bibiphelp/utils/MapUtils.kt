package com.github.frayeralex.bibiphelp.utils

import android.app.Activity
import android.content.res.Resources
import com.github.frayeralex.bibiphelp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions

object MapUtils {
    fun updateStyle(context: Activity, mMap: GoogleMap) {
        try {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map_styles
                )
            )
        } catch (e: Resources.NotFoundException) {
        }
    }

    fun updateMapCamera(mMap: GoogleMap, markers: List<LatLng>, zoomLevel: Float? = null) {
        if (markers.isEmpty()) return
        val builder = LatLngBounds.builder()

        markers.forEach { builder.include(it) }

        if (zoomLevel !== null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(builder.build().center, zoomLevel))
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
        }
    }
}