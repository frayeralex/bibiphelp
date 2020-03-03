package com.github.frayeralex.bibiphelp.repository

import com.github.frayeralex.bibiphelp.constatns.Collections
import com.google.firebase.database.FirebaseDatabase

object FBRefs {
    private val db = FirebaseDatabase.getInstance()

    val categoriesRef = db.getReference(Collections.CATEGORIES)

    val eventsRef = db.getReference(Collections.EVENTS)

    val activeEventsRef = eventsRef.orderByChild("status").equalTo(0.toDouble())
}