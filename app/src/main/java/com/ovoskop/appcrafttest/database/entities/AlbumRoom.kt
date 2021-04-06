package com.ovoskop.appcrafttest.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumRoom(

    @ColumnInfo(name = "user_id")
    var userId: Int? = null,

    @PrimaryKey
    var id: Int? = null,

    var title: String? = null

)