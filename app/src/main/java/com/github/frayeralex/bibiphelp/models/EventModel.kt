package com.github.frayeralex.bibiphelp.models

import com.github.frayeralex.bibiphelp.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.IgnoreExtraProperties
//todo update - not final model schema
@IgnoreExtraProperties
data class EventModel(
    var id: String? = "",
    var type: String? = "",
    var message: String? = "",
    var lat: Double? = 0.0,
    var long: Double? = 0.0
) {
    private fun getPin(): BitmapDescriptor = when (type) {
        TYPE_1 -> BitmapDescriptorFactory.fromResource(R.drawable.pin_1)
        TYPE_2 -> BitmapDescriptorFactory.fromResource(R.drawable.pin_2)
        TYPE_3 -> BitmapDescriptorFactory.fromResource(R.drawable.pin_3)
        TYPE_4 -> BitmapDescriptorFactory.fromResource(R.drawable.pin_4)
        TYPE_5 -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
        else -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
    }

    private fun getCoordinates(): LatLng = LatLng(lat!!, long!!)

    fun getMapMarker(): MarkerOptions = MarkerOptions()
        .position(getCoordinates())
        .title(message)
        .anchor((0.5).toFloat(), (0.5).toFloat())
        .icon(getPin())

    companion object {
        const val TYPE_1 = "type_1"
        const val TYPE_2 = "type_2"
        const val TYPE_3 = "type_3"
        const val TYPE_4 = "type_4"
        const val TYPE_5 = "type_5"
    }
}