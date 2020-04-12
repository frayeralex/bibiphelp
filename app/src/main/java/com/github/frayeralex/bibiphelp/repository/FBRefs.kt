package com.github.frayeralex.bibiphelp.repository

import com.github.frayeralex.bibiphelp.constatns.Collections
import com.github.frayeralex.bibiphelp.constatns.EventStatuses
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

interface FBRefsInterface {
    val categoriesRef : DatabaseReference
    val eventsRef : DatabaseReference
    val activeEventsRef : Query
}

object FBRefs: FBRefsInterface {
    private val db = FirebaseDatabase.getInstance()

    init {
        db.setPersistenceEnabled(true)
    }

    const val EVENT_STATUS = "status"
    const val EVENT_HELPERS = "helpers"

    override val categoriesRef = db.getReference(Collections.CATEGORIES)

    override val eventsRef = db.getReference(Collections.EVENTS)

    override val activeEventsRef =
        eventsRef.orderByChild(EVENT_STATUS).equalTo(EventStatuses.ACTIVE.toDouble())
}