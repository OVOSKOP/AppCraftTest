package com.ovoskop.appcrafttest.ui.notifications

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.utils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.ovoskop.appcrafttest.utils.services.LocationService

class LocationFragment : Fragment() {

    private lateinit var locStart: Button
    private lateinit var coords: TextView

    private var isServiceRun = false
    private var isFollow = false
    private var mLocationPermissionGranted = false

    private var mService: LocationService? = null

    private val connection: ServiceConnection = object  : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isServiceRun = true
            isFollow = true

            locStart.text = "Стоп"

            val binder = service as LocationService.LocalBinder
            mService = binder.getService()
            coords.text = getString(R.string.coords, mService?.lastLocation?.latitude, mService?.lastLocation?.longitude)

            mService?.setOnLocationListener {
                coords.text = getString(R.string.coords, it.latitude, it.longitude)
            }
            coords.visibility = View.VISIBLE
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceRun = false
        }

        override fun onNullBinding(name: ComponentName?) {
            isServiceRun = false
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        getLocationPermission()

        val root = inflater.inflate(R.layout.fragment_location, container, false)

        locStart = root.findViewById(R.id.location_show)
        coords = root.findViewById(R.id.coords_location)

        val service = Intent(requireContext(), LocationService::class.java)
        requireActivity().bindService(service, connection, 0)

        locStart.setOnClickListener {
            if (mLocationPermissionGranted) {

                if (isFollow) {
                    stopService()
                    locStart.text = "Отслеживание"
                    coords.visibility = View.INVISIBLE
                } else {
                    startService()
                    locStart.text = "Стоп"
                }
                isFollow = !isFollow

            }
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()

        mService?.removeLocationListener()
    }

    private fun stopService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireActivity().unbindService(connection)
        requireActivity().stopService(intent)
    }

    private fun startService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireActivity().startService(intent)
        requireActivity().bindService(intent, connection, 0)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        getSettingsPermissions()
                    }
                }
            }
        }
    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("per", "true1")
            mLocationPermissionGranted = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("TESTPER", "HELLO")
                getSettingsPermissions()
            }
        } else {
            Log.d("per", "false1")
            mLocationPermissionGranted = false
            getPermissions()
        }
    }

    private fun getPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getSettingsPermissions() {
        if (requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            getAlert()
        }
    }

    private fun getAlert() {

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Требуется дополнительное разрешение")
            .setMessage("Для полной функциональности приложения необходимо разрешение получения вашего местоположения, когда приложение закрыто.")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Включить", null)
            .setNegativeButton("Отмена", null)
            .create()

        alertDialog.setOnShowListener {
            Log.d("openAlert", "OPEN")
            val positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveBtn.setOnClickListener {
                alertDialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                }
            }
            negativeBtn.setOnClickListener {
                alertDialog.dismiss()

            }
        }

        alertDialog.show()

    }

}