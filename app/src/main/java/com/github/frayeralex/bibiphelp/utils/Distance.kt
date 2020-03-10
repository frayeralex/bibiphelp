package com.github.frayeralex.bibiphelp.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object DistanceCalculator {
    @JvmStatic
    fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: String
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
            if (unit == "K") {
                dist *= 1.609344
            } else if (unit == "N") {
                dist *= 0.8684
            }
            dist
        }
    }

    @JvmStatic
    fun formatDistance(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value)
    }
}