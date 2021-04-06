package com.ovoskop.appcrafttest.utils.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ovoskop.appcrafttest.utils.services.LocationService

class RestartedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, LocationService::class.java))
        } else {
            context.startService(Intent(context, LocationService::class.java))
        }
    }
}