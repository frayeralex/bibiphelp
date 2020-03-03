package com.github.frayeralex.bibiphelp.models

import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EventModel(
    var id: String? = "",
    var type: Int? = EventTypes.OTHER,
    var message: String? = "",
    var createdAt: Long? = 0,
    var userId: String? = "",
    var lat: Double? = 0.0,
    var long: Double? = 0.0,
    var status: Int? = 0,
    val helpers: MutableMap<String, Boolean> = HashMap()
)