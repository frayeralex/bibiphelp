package com.github.frayeralex.bibiphelp.models

import com.github.frayeralex.bibiphelp.R


data class EventCategoryModel (
    var id: Int? = OTHER,
    var label: String? = ""
) {
    fun getImgResource(): Int = when (id) {
        OIL -> R.drawable.category_1_icon
        WHEEL -> R.drawable.category_2_icon
        ENERGY -> R.drawable.category_3_icon
        SNOW -> R.drawable.category_4_icon
        TOWING -> R.drawable.category_5_icon
        OTHER -> R.drawable.category_6_icon
        else -> R.drawable.category_6_icon
    }

    companion object {
        const val OIL = 1
        const val WHEEL = 2
        const val ENERGY = 3
        const val SNOW = 4
        const val TOWING = 5
        const val OTHER = 6
    }
}