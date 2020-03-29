package com.github.frayeralex.bibiphelp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Error

class CategoriesViewModel : ViewModel() {
    private val categories: MutableLiveData<MutableList<EventCategoryModel>> = MutableLiveData()

    private val categoriesRequestStatus: MutableLiveData<String> =
        MutableLiveData(RequestStatuses.UNCALLED)

    fun getCategories(): LiveData<MutableList<EventCategoryModel>> {
        if (categories.value === null) {
            categoriesRequestStatus.value = RequestStatuses.PENDING

            FBRefs.categoriesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val categoriesList = mutableListOf<EventCategoryModel>()

                    for (eventSnapshot in dataSnapshot.children) {
                        try {
                            val category = eventSnapshot.getValue(EventCategoryModel::class.java)
                            if (category is EventCategoryModel) {
                                categoriesList.add(category)
                            }
                        } catch (error: Error) {
                            Log.d(TAG, error.toString())
                        }
                    }

                    categories.value = categoriesList
                    categoriesRequestStatus.value = RequestStatuses.SUCCESS
                }

                override fun onCancelled(error: DatabaseError) {
                    categoriesRequestStatus.value = RequestStatuses.FAILURE
                }
            })
        }
        return categories
    }

    fun getCategoriesRequestStatus() = categoriesRequestStatus

    companion object {
        const val TAG = "CategoriesViewModel"
    }
}