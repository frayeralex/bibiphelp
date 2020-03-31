package com.github.frayeralex.bibiphelp.utils

import org.junit.Test
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.models.EventModel

import org.junit.Assert.*

class EventCategoryModelUtilsTest {

    @Test
    fun getImgResource() {
        assertEquals(
            R.drawable.category_1_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.OIL))
        )
        assertEquals(
            R.drawable.category_2_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.WHEEL))
        )
        assertEquals(
            R.drawable.category_3_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.ENERGY))
        )
        assertEquals(
            R.drawable.category_4_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.SNOW))
        )
        assertEquals(
            R.drawable.category_5_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.TOWING))
        )
        assertEquals(
            R.drawable.category_6_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = EventTypes.OTHER))
        )
        assertEquals(
            R.drawable.category_6_icon,
            EventCategoryModelUtils.getImgResource(EventCategoryModel(id = 999))
        )
    }

    @Test
    fun getTypeColor() {
        assertEquals(
            R.color.run_out_of_fuel,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.OIL))
        )
        assertEquals(
            R.color.wheel_replacement,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.WHEEL))
        )
        assertEquals(
            R.color.low_battery,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.ENERGY))
        )
        assertEquals(
            R.color.stuck_in_the_snow,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.SNOW))
        )
        assertEquals(
            R.color.towing,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.TOWING))
        )
        assertEquals(
            R.color.other,
            EventCategoryModelUtils.getTypeColor(EventModel(type = EventTypes.OTHER))
        )
        assertEquals(
            R.color.other,
            EventCategoryModelUtils.getTypeColor(EventModel(type = 99))
        )
    }

    @Test
    fun getTypeLabel() {
        assertEquals(
            R.string.srun_out_of_fuel,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = EventTypes.OIL))
        )
        assertEquals(
            R.string.sheel_replacement,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = EventTypes.WHEEL))
        )
        assertEquals(
            R.string.slow_battery,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = EventTypes.ENERGY))
        )
        assertEquals(
            R.string.sstuck_in_the_snow,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = EventTypes.SNOW))
        )
        assertEquals(
            R.string.stowing,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = EventTypes.TOWING))
        )
        assertEquals(
            R.string.sother,
            EventCategoryModelUtils.getTypeLabel(EventModel(type = 99))
        )
    }
}