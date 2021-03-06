package com.github.frayeralex.bibiphelp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.models.*
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class WaitHelpViewModel : ViewModel() {
    private val event: MutableLiveData<EventModel> = MutableLiveData()

    private val eventRequestStatus: MutableLiveData<String> =
        MutableLiveData(RequestStatuses.UNCALLED)

    fun getEvent(eventId: String): LiveData<EventModel> {
        if (event.value == null) {
            FBRefs.eventsRef.child(eventId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        event.value = dataSnapshot.getValue(EventModel::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        return event
    }

    fun getEventRequestStatus() = eventRequestStatus
}