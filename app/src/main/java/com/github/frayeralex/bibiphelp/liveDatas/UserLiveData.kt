package com.github.frayeralex.bibiphelp.liveDatas

import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.LiveData

import com.google.firebase.auth.FirebaseUser

class UserLiveData : LiveData<FirebaseUser>() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onActive() {
        super.onActive()

        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        value = auth.currentUser
                    }
                }
        }

        value = auth.currentUser
    }
}