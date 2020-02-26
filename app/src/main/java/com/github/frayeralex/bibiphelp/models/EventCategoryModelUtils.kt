package com.github.frayeralex.bibiphelp.models

import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.EventTypes

object EventCategoryModelUtils {
    @JvmStatic
    fun getImgResource(eventCategory: EventCategoryModel): Int = when (eventCategory.id) {
        EventTypes.OIL -> R.drawable.category_1_icon
        EventTypes.WHEEL -> R.drawable.category_2_icon
        EventTypes.ENERGY -> R.drawable.category_3_icon
        EventTypes.SNOW -> R.drawable.category_4_icon
        EventTypes.TOWING -> R.drawable.category_5_icon
        EventTypes.OTHER -> R.drawable.category_6_icon
        else -> R.drawable.category_6_icon
    }
}