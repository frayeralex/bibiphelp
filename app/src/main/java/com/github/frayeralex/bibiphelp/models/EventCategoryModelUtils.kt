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
    @JvmStatic
    fun getTypeColor(event: EventModel) = when (event.type) {
        EventTypes.OIL -> R.color.run_out_of_fuel
        EventTypes.WHEEL -> R.color.wheel_replacement
        EventTypes.ENERGY -> R.color.low_battery
        EventTypes.SNOW -> R.color.stuck_in_the_snow
        EventTypes.TOWING -> R.color.towing
        else -> R.color.other
    }
    @JvmStatic
    fun getTypeLabel(event: EventModel) = when (event.type) {
        EventTypes.OIL -> R.string.srun_out_of_fuel
        EventTypes.WHEEL -> R.string.sheel_replacement
        EventTypes.ENERGY -> R.string.slow_battery
        EventTypes.SNOW -> R.string.sstuck_in_the_snow
        EventTypes.TOWING -> R.string.stowing
        else -> R.string.sother
    }
}