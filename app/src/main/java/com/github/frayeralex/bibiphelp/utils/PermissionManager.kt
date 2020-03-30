package com.github.frayeralex.bibiphelp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.frayeralex.bibiphelp.R

object PermissionManager {
    fun checkLocationPermission(context: Activity, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // todo: replace into Required Permissions screen for better UX
                Toast.makeText(
                    context, R.string.app_location_permission,
                    Toast.LENGTH_LONG
                ).show()

            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode
                )
            }
        }
    }
}