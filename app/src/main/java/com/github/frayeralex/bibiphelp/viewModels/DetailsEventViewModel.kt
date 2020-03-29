package com.github.frayeralex.bibiphelp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.liveDatas.UserLiveData
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DetailsEventViewModel : ViewModel() {
    private val user = UserLiveData()
    private val event: MutableLiveData<EventModel> = MutableLiveData()
    private val category: MutableLiveData<EventCategoryModel> = MutableLiveData()
    private val helpRequestStatus = MutableLiveData(RequestStatuses.UNCALLED)
    private val helperRejectRequestStatus = MutableLiveData(RequestStatuses.UNCALLED)

    fun getUser() = user

    fun getEvent(eventId: String): LiveData<EventModel?> {
        if (event.value == null) {
            FBRefs.eventsRef.child(eventId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {
                            val eventData = dataSnapshot.getValue(EventModel::class.java)
                            event.value = eventData

                            FBRefs.categoriesRef.child(eventData?.type.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            category.value =
                                                dataSnapshot.getValue(EventCategoryModel::class.java)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
        return event
    }

    fun getRequestStatus() = helpRequestStatus

    fun sendHelpRequest(eventId: String, userId: String) {
        val helpersRef = FBRefs.eventsRef.child(eventId).child(FBRefs.EVENT_HELPERS)
        val action = helpersRef.child(userId).setValue(true)

        helpRequestStatus.value = RequestStatuses.PENDING

        action.addOnCompleteListener {
            if (it.exception != null) {
                helpRequestStatus.value = RequestStatuses.FAILURE
            } else {
                helpRequestStatus.value = RequestStatuses.SUCCESS
            }
        }
    }

    fun rejectHelperRequest(eventId: String, userId: String) {
        val helpersRef = FBRefs.eventsRef.child(eventId).child(FBRefs.EVENT_HELPERS)
        val action = helpersRef.child(userId).removeValue()

        helperRejectRequestStatus.value = RequestStatuses.PENDING

        action.addOnCompleteListener {
            if (it.exception != null) {
                helperRejectRequestStatus.value = RequestStatuses.FAILURE
            } else {
                helperRejectRequestStatus.value = RequestStatuses.SUCCESS
            }
        }
    }

    fun getHelperRejectRequestStatus() = helperRejectRequestStatus

    fun getCategory() = category
}