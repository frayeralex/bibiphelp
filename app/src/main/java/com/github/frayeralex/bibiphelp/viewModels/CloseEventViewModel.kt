package com.github.frayeralex.bibiphelp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.repository.FBRefs

class CloseEventViewModel : ViewModel() {
    private val isRequesting = MutableLiveData(false)
    private val isClosed = MutableLiveData(false)

    fun getRequestingState() = isRequesting
    fun getIsClosed() = isClosed

    fun closeEvent(eventId: String, code: Int) {
        isRequesting.value = true

        val statusRef = FBRefs.eventsRef.child(eventId).child("status")

        val action = statusRef.setValue(code)

        action.addOnSuccessListener {
            isClosed.value = true
            isRequesting.value = false
        }
        action.addOnCanceledListener {
            isRequesting.value = false
        }
    }
}