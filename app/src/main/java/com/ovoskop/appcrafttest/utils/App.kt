package com.ovoskop.appcrafttest.utils

import android.app.Application
import androidx.room.Room
import com.ovoskop.appcrafttest.database.AppDatabase

class App : Application() {

    private lateinit var _database: AppDatabase
    val database: AppDatabase
        get() = _database

    override fun onCreate() {
        super.onCreate()

        _database = Room.databaseBuilder(this, AppDatabase::class.java, "db_saved_albums")
            .fallbackToDestructiveMigration()
            .build()
    }

}