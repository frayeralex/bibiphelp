package com.github.frayeralex.bibiphelp.viewModels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.InputValidation
import com.github.frayeralex.bibiphelp.liveDatas.LocationLiveData
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.repository.FBRefs

class HelpFormViewModel(application: Application) : AndroidViewModel(application) {

    private val validationErrorCode: MutableLiveData<Int?> = MutableLiveData(null)
    private val message: MutableLiveData<String> = MutableLiveData("")

    private val location = LocationLiveData(application)

    private val isRequesting = MutableLiveData(false)
    private val newCreatedEventId = MutableLiveData<String?>(null)

    fun getLocationData() = location
    fun getRequestingState() = isRequesting
    fun getCreatedEventId() = newCreatedEventId
    fun getMessage() = message
    fun getValidationError() = validationErrorCode

    fun setMessage(value: String) {
        message.value = value

        validationErrorCode.value = when {
            value.length > InputValidation.helpMsgMaxLength -> R.string.help_input_large_text
            value.isEmpty() -> R.string.help_input_label_empty
            else -> null
        }
    }

    fun createHelpRequest(message: String, categoryId: Int, location: Location) {
        isRequesting.value = true

        val eventRef = FBRefs.eventsRef.push()
        val eventId = eventRef.key

        val action = eventRef.setValue(EventModel(
            id = eventRef.key,
            message = message,
            type = categoryId,
            long = location.longitude,
            lat = location.latitude
        ))

        action.addOnSuccessListener {
            isRequesting.value = false
            newCreatedEventId.value = eventId
        }
        action.addOnCanceledListener { isRequesting.value = false }
    }


}