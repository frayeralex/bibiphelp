package com.github.frayeralex.bibiphelp.repository

import com.github.frayeralex.bibiphelp.constatns.Collections
import com.github.frayeralex.bibiphelp.constatns.EventStatuses
import com.google.firebase.database.FirebaseDatabase

object FBRefs {
    val db = FirebaseDatabase.getInstance()
    const val statusField = "status"
    const val helpersMapField = "helpers"

    val categoriesRef = db.getReference(Collections.CATEGORIES)

    val eventsRef = db.getReference(Collections.EVENTS)

    val activeEventsRef = eventsRef.orderByChild(statusField).equalTo(EventStatuses.ACTIVE.toDouble())
}