package com.ovoskop.appcrafttest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ovoskop.appcrafttest.database.entities.AlbumRoom

@Dao
interface AlbumDAO {

    @Query("SELECT * FROM albums")
    fun getAll() : LiveData<List<AlbumRoom>>

    @Query("SELECT * FROM albums WHERE id = :id")
    fun getById(id: Int) : LiveData<AlbumRoom>

    @Insert
    fun insert(album: AlbumRoom)

    @Update
    fun update(album: AlbumRoom)

    @Delete
    fun delete(album: AlbumRoom)

}