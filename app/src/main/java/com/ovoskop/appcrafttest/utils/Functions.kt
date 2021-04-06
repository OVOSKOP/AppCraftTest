package com.ovoskop.appcrafttest.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import com.ovoskop.appcrafttest.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

val Context.app : App
    get() = this.applicationContext as App

fun getWidthImage(context: Context, span: Int): Int {
    val metrics = DisplayMetrics()
    context as Activity

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val display = context.display
        display?.getRealMetrics(metrics)
    } else {
        @Suppress("DEPRECATION")
        val display = context.windowManager.defaultDisplay
        @Suppress("DEPRECATION")
        display.getMetrics(metrics)
    }

    val width = metrics.widthPixels

    return (width - (context.resources.getDimensionPixelSize(R.dimen.previews_margin) * (span + 1))) / span
}


private fun isExternalStorageWritable() : Boolean {
    val state: String = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state
}

private fun saveTempBitmap(filename: String, bitmap: Bitmap?, context: Context, size: String) : String {
    if (isExternalStorageWritable()) {
        if (bitmap != null) {
            return saveImage(filename, bitmap, context, size)
        }
        return ""
    } else {
        println("WHAT")
        return ""
    }
}

private fun saveImage(filename: String, finalBitmap: Bitmap, context: Context, size: String) : String {
    val root = context.getExternalFilesDir(null)?.absolutePath
    val myDir = File("$root/saved_images/$size")
    myDir.mkdirs()

    val fName = "$filename.png"
    val file = File(myDir, fName)
    if (file.exists()) file.delete()

    return try {
        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()

        fName
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        println(e.localizedMessage)
        ""
    }
}

fun saveImage(context: Context, url: String?, size: String) : String {
    if (url != null) {
        val filename = url.split("/").last()


        val bitmap = Picasso.get()
            .load(url)
            .get()

        return saveTempBitmap(filename, bitmap, context, size)
    }
    return ""
}

fun getFileUri(context: Context, fName: String?, size: String) : File {
    val root = context.getExternalFilesDir(null)?.absolutePath
    val myDir = File("$root/saved_images/$size")

    return File(myDir, fName.toString())
}

fun isNetworkConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT < 23) {
        val ni = cm.activeNetworkInfo
        if (ni != null) {
            return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
        }
    } else {
        val n: Network? = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}