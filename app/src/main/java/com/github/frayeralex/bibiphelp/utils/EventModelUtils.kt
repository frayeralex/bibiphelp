package com.github.frayeralex.bibiphelp.utils

import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object EventModelUtils {
    @JvmStatic
    fun getPin(event: EventModel): BitmapDescriptor = when (event.type) {
        EventTypes.OIL -> BitmapDescriptorFactory.fromResource(R.drawable.pin_1)
        EventTypes.WHEEL -> BitmapDescriptorFactory.fromResource(R.drawable.pin_2)
        EventTypes.ENERGY -> BitmapDescriptorFactory.fromResource(R.drawable.pin_3)
        EventTypes.SNOW -> BitmapDescriptorFactory.fromResource(R.drawable.pin_4)
        EventTypes.TOWING -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
        else -> BitmapDescriptorFactory.fromResource(R.drawable.pin_5)
    }

    @JvmStatic
    fun getCoordinates(event: EventModel): LatLng = LatLng(event.lat!!, event.long!!)

    @JvmStatic
    fun getMapMarker(event: EventModel, title: String? = event.message): MarkerOptions = MarkerOptions()
        .position(
            getCoordinates(
                event
            )
        )
        .title(title)
        .anchor((0.5).toFloat(), (0.5).toFloat())
        .icon(getPin(event))
}
