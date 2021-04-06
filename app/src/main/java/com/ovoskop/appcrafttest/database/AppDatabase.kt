package com.ovoskop.appcrafttest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ovoskop.appcrafttest.database.dao.AlbumDAO
import com.ovoskop.appcrafttest.database.dao.PhotoDAO
import com.ovoskop.appcrafttest.database.entities.AlbumRoom
import com.ovoskop.appcrafttest.database.entities.PhotoRoom

@Database(entities = [AlbumRoom::class, PhotoRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDAO() : AlbumDAO
    abstract fun photoDAO() : PhotoDAO
}