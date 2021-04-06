package com.ovoskop.appcrafttest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ovoskop.appcrafttest.database.entities.PhotoRoom

@Dao
interface PhotoDAO {

    @Query("SELECT * FROM previews")
    fun getAll() : LiveData<List<PhotoRoom>>

    @Query("SELECT * FROM previews WHERE id = :id")
    fun getById(id: Int) : LiveData<PhotoRoom>

    @Query("SELECT * FROM previews WHERE album_id = :albumId")
    fun getByAlbum(albumId: Int) : LiveData<List<PhotoRoom>>

    @Insert
    fun insert(preview: PhotoRoom)

    @Update
    fun update(preview: PhotoRoom)

    @Delete
    fun delete(preview: PhotoRoom)

}