package com.github.frayeralex.bibiphelp.constatns

import org.junit.Test
import org.junit.Assert.*

class EventStatusesTest {
    @Test
    fun checkValues() {
        assertEquals(EventStatuses.ACTIVE, 0)
        assertEquals(EventStatuses.SUCCESS, 1)
        assertEquals(EventStatuses.SELF, 2)
        assertEquals(EventStatuses.NONACTUAL, 3)
    }
}