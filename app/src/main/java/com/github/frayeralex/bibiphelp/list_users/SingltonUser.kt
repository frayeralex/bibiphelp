package com.github.frayeralex.bibiphelp.list_users

import android.util.Log
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.models.EventModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object SingltonUser {

    const val DB_EVENTS = "events"
    private var eventsRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference(DB_EVENTS)
    val mlistEvents: ArrayList<EventModel?> = ArrayList()

    init {
        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (eventSnapshot in dataSnapshot.children) {
                    val event = eventSnapshot.getValue(EventModel::class.java)
                    mlistEvents.add(event)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(MainActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun getListEvent(): ArrayList<EventModel?> {
        return mlistEvents
    }


    fun getId(id: String?): EventModel? {
        for (n in mlistEvents) if (n?.id == id) return n
        return null
    }
}