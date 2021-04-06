package com.ovoskop.appcrafttest.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ovoskop.appcrafttest.utils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION

class PermissionsController(private val fragment: Fragment) {

    fun getLocationPermission() : Boolean {
        return if (ContextCompat.checkSelfPermission(
                        fragment.requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getSettingsPermissions()
            }

            true
        } else {
            getPermissions()
            false
        }
    }

    private fun getPermissions() {
       fragment.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getSettingsPermissions() {
        if (fragment.requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            AlertController(fragment).getAlert()
        }
    }

}