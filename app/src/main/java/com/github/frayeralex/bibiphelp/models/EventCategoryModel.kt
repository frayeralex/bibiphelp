package com.github.frayeralex.bibiphelp.models

import com.github.frayeralex.bibiphelp.constatns.EventTypes

data class EventCategoryModel(
    var id: Int? = EventTypes.OTHER,
    var label: String? = ""
)