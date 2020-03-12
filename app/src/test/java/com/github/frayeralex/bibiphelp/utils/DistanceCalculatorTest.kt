package com.github.frayeralex.bibiphelp.utils

import org.junit.Test
import org.junit.Assert.*

class DistanceCalculatorTest {

    @Test
    fun distance() {
        assertEquals(
            "1.3790152781778036",
            DistanceCalculator.distance(49.12312, -23.3434, 49.12221, -23.3245).toString()
        )
    }

    @Test
    fun formatDistance() {
        assertEquals("1.06", DistanceCalculator.formatDistance(1.05555))
        assertEquals("1.5", DistanceCalculator.formatDistance(1.5001))
        assertEquals("1", DistanceCalculator.formatDistance(1.0001))
    }
}