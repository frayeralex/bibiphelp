package com.github.frayeralex.bibiphelp.viewModels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.liveDatas.LocationLiveData
import com.github.frayeralex.bibiphelp.liveDatas.UserLiveData
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HelpFormViewModel(application: Application) : AndroidViewModel(application) {
    private val category : MutableLiveData<EventCategoryModel> = MutableLiveData()

    private val location = LocationLiveData(application)

    private val isRequesting = MutableLiveData(false)
    private val isCreatedSuccess = MutableLiveData(false)

    fun getCategory(categoryId: String): LiveData<EventCategoryModel> {
        if (category.value === null) {


            FBRefs.categoriesRef.child(categoryId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    category.value = dataSnapshot.getValue(EventCategoryModel::class.java)
                }

                override fun onCancelled(error: DatabaseError) {  }
            })
        }
        return category
    }

    fun getLocationData() = location
    fun getRequestingState() = isRequesting
    fun getCreatedSuccess() = isCreatedSuccess

    fun createHelpRequest(message: String, categoryId: Int, location: Location) {
        isRequesting.value = true

        val eventRef = FBRefs.eventsRef.push()

        val action = eventRef.setValue(EventModel(
            id = eventRef.key,
            message = message,
            type = categoryId,
            long = location.longitude,
            lat = location.latitude
        ))

        action.addOnSuccessListener {
            isRequesting.value = false
            isCreatedSuccess.value = true
        }
        action.addOnCanceledListener { isRequesting.value = false }
    }

    companion object {
        const val TAG = "HelpFormViewModel"
    }
}