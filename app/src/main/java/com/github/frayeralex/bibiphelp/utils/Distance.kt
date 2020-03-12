package com.github.frayeralex.bibiphelp.utils

import java.text.DecimalFormat

const val KILOMETERS = "K"

object DistanceCalculator {
    fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: String = KILOMETERS
    ): Double {
        return if (lat1 == lat2 && lon1 == lon2) {
           0.toDouble()
        } else {
            val theta = lon1 - lon2
            var dist =
                Math.sin(Math.toRadians(lat1)) * Math.sin(
                    Math.toRadians(lat2)
                ) + Math.cos(Math.toRadians(lat1)) * Math.cos(
                    Math.toRadians(
                        lat2
                    )
                ) * Math.cos(Math.toRadians(theta))
            dist = Math.acos(dist)
            dist = Math.toDegrees(dist)
            dist *= 60 * 1.1515
            if (unit == KILOMETERS) {
                dist *= 1.609344
            }
            dist
        }
    }

    fun formatDistance(value: Double): String {
        val df = DecimalFormat("#.##")
        return df.format(value)
    }
}