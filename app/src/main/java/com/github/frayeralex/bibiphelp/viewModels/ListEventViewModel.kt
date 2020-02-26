package com.github.frayeralex.bibiphelp.viewModels

import com.github.frayeralex.bibiphelp.models.EventModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Error

class ListEventViewModel : ViewModel() {
    private val events: MutableLiveData<MutableList<EventModel>> = MutableLiveData()

    private val eventsStatus: MutableLiveData<String> = MutableLiveData(RequestStatuses.UNCALLED)

    fun getEvents(): LiveData<MutableList<EventModel>> {
        if (events.value === null) {
            eventsStatus.value = RequestStatuses.PENDING

            FBRefs.eventsRef.addValueEventListener(object : ValueEventListener {
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
                    eventsStatus.value = RequestStatuses.SUCCESS
                }

                override fun onCancelled(error: DatabaseError) {
                    eventsStatus.value = RequestStatuses.FAILURE
                }
            })
        }
        return events
    }

    companion object {
        const val TAG = "ListEventViewModel"
    }
}