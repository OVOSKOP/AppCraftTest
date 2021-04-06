package com.ovoskop.appcrafttest.utils.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.ovoskop.appcrafttest.R
import com.ovoskop.appcrafttest.activity.MainActivity
import com.ovoskop.appcrafttest.utils.CHANNEL_ID
import com.ovoskop.appcrafttest.utils.NOTIFICATION_ID

class LocationService : Service() {

    private val binder = LocalBinder()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mAudio: MediaPlayer

    private var locationListener: ((LatLng) -> Unit)? = null
    var lastLocation: LatLng = LatLng(0.0, 0.0)

    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            println("RESULT")
            if (locationResult != null) {
                for (location in locationResult.locations) {
                    Log.d("MyService", location.toString())
                    val latLng = LatLng(location.latitude, location.longitude)

                    lastLocation = latLng
                    locationListener?.invoke(latLng)

                    val notification = getNotification("Отслеживание геолокации ${location.latitude}/${location.longitude}")

                    val mNotificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    mNotificationManager.notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("MyService", "Creating...")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mAudio = MediaPlayer.create(this, R.raw.audio)
        mAudio.isLooping = true

        mAudio.start()

        startMyOwnForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getLocation()
        Log.d("MyService", "GET LOCATION")

        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        Log.d("MyService", "Destroy....")
        fusedLocationClient.removeLocationUpdates(locationCallback)
        mAudio.stop()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    @Throws(SecurityException::class)
    private fun getLocation() {

        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.result == null) {
                getCurrentLocation()
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                interval = 3000
                fastestInterval = 1000
                maxWaitTime = 5000
            },
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @Throws(SecurityException::class)
    private fun getCurrentLocation() {

        val cancel = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancel.token)
            .addOnSuccessListener {
                val latLng = LatLng(it.latitude, it.longitude)
                lastLocation = latLng

                cancel.cancel()
            }

    }

    private fun startMyOwnForeground() {
        val notification = getNotification("Отслеживание геолокации")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification)
        } else {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        }
    }

    private fun getNotification(text: String) : Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentTitle("AppCraftTest")
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        } else {
            notificationBuilder
                .build()
        }
    }

    fun setOnLocationListener(listener: (LatLng) -> Unit) {
        locationListener = listener
    }

    fun removeLocationListener() {
        locationListener = null
    }

}