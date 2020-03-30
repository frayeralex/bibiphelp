package com.github.frayeralex.bibiphelp.constatns

import org.junit.Assert.*
import org.junit.Test

class EventTypesTest {
    @Test
    fun checkValues() {
        assertEquals(3, EventTypes.ENERGY)
        assertEquals(1, EventTypes.OIL)
        assertEquals(2, EventTypes.WHEEL)
        assertEquals(4, EventTypes.SNOW)
        assertEquals(5, EventTypes.TOWING)
        assertEquals(6, EventTypes.OTHER)
    }
}