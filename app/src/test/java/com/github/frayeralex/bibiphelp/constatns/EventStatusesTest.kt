package com.github.frayeralex.bibiphelp.constatns

import org.junit.Test
import org.junit.Assert.*

class EventStatusesTest {
    @Test
    fun checkValues() {
        assertEquals(0, EventStatuses.ACTIVE)
        assertEquals(1, EventStatuses.SUCCESS)
        assertEquals(2, EventStatuses.SELF)
        assertEquals(3, EventStatuses.NONACTUAL)
    }
}