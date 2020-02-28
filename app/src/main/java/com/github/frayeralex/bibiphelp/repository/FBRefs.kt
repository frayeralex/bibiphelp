package com.github.frayeralex.bibiphelp.repository

import com.github.frayeralex.bibiphelp.constatns.Collections
import com.google.firebase.database.FirebaseDatabase

object FBRefs {
    val categoriesRef = FirebaseDatabase.getInstance().getReference(Collections.CATEGORIES)
    val eventsRef = FirebaseDatabase.getInstance().getReference(Collections.EVENTS)
}