package com.github.frayeralex.bibiphelp.repository

import com.github.frayeralex.bibiphelp.constatns.Collections
import com.github.frayeralex.bibiphelp.constatns.EventStatuses
import com.google.firebase.database.FirebaseDatabase

object FBRefs {
    private val db = FirebaseDatabase.getInstance()

    init {
        db.setPersistenceEnabled(true)
    }

    const val EVENT_STATUS = "status"
    const val EVENT_HELPERS = "helpers"

    val categoriesRef = db.getReference(Collections.CATEGORIES)

    val eventsRef = db.getReference(Collections.EVENTS)

    val activeEventsRef =
        eventsRef.orderByChild(EVENT_STATUS).equalTo(EventStatuses.ACTIVE.toDouble())
}