package com.github.frayeralex.bibiphelp.viewModels

import android.app.Application
import com.github.frayeralex.bibiphelp.models.EventModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.liveDatas.LocationLiveData
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ConfirmedHelpViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    private val eventsStatus: MutableLiveData<String> = MutableLiveData(RequestStatuses.UNCALLED)

    private val event : MutableLiveData<EventModel> = MutableLiveData()

    fun getEvent(eventId: String) : LiveData<EventModel> {
        if (event.value == null) {
            eventsStatus.value = RequestStatuses.PENDING
            FBRefs.eventsRef.child(eventId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        event.value = dataSnapshot.getValue(EventModel::class.java)
                        eventsStatus.value = RequestStatuses.SUCCESS
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    eventsStatus.value = RequestStatuses.FAILURE
                }
            })
        }
        return event
    }

    fun getLocationData() = locationData
}