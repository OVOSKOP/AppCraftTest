package com.ovoskop.appcrafttest.ui.location

import android.Manifest
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.content.Context
import android.os.Build
import android.util.Log
import com.ovoskop.appcrafttest.utils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION

class AlertController(private val fragment: Fragment) {

    fun getAlert() {

        val alertDialog = AlertDialog.Builder(fragment.requireContext())
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
                    fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                }
            }
            negativeBtn.setOnClickListener {
                alertDialog.dismiss()

            }
        }

        alertDialog.show()

    }

}