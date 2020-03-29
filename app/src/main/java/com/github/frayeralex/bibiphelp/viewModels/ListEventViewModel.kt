package com.github.frayeralex.bibiphelp.viewModels

import android.app.Application
import com.github.frayeralex.bibiphelp.models.EventModel
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.liveDatas.LocationLiveData
import com.github.frayeralex.bibiphelp.liveDatas.UserLiveData
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Error

class ListEventViewModel(application: Application) : AndroidViewModel(application) {
    private val events: MutableLiveData<MutableList<EventModel>> = MutableLiveData()

    private val locationData = LocationLiveData(application)

    private val user = UserLiveData()

    private val eventsRequestStatus: MutableLiveData<String> =
        MutableLiveData(RequestStatuses.UNCALLED)

    fun getEvents(): LiveData<MutableList<EventModel>> {
        if (events.value === null) {
            eventsRequestStatus.value = RequestStatuses.PENDING

            FBRefs.activeEventsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val eventsList = mutableListOf<EventModel>()

                    for (eventSnapshot in dataSnapshot.children) {
                        try {
                            val event = eventSnapshot.getValue(EventModel::class.java)
                            if (event is EventModel) {
                                eventsList.add(event)
                            }
                        } catch (error: Error) {
                            Log.d(TAG, error.toString())
                        }
                    }

                    events.value = eventsList
                    eventsRequestStatus.value = RequestStatuses.SUCCESS
                }

                override fun onCancelled(error: DatabaseError) {
                    eventsRequestStatus.value = RequestStatuses.FAILURE
                }
            })
        }
        return events
    }

    fun getLocationData() = locationData

    fun getUser() = user

    fun getEventsRequestStatus() = eventsRequestStatus

    companion object {
        const val TAG = "ListEventViewModel"
    }
}