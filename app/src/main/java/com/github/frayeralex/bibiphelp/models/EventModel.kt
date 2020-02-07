package com.github.frayeralex.bibiphelp.models

import com.google.firebase.database.IgnoreExtraProperties
//todo update - not final model schema
@IgnoreExtraProperties
data class EventModel(
    var id: String? = "",
    var type: String? = "",
    var message: String? = "",
    var lat: Double? = 0.0,
    var long: Double? = 0.0
)