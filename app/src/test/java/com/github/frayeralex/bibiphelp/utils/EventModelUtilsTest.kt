package com.github.frayeralex.bibiphelp.utils

import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

class EventModelUtilsTest {

    @Test
    fun getPin() {
    }

    @Test
    fun getCoordinates() {
        assertEquals(
            LatLng(0.0, 0.0),
            EventModelUtils.getCoordinates(EventModel(lat = 0.0, long = 0.0))
        )
    }

    @Test
    fun getMapMarker() {
    }
}