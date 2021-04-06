package com.ovoskop.appcrafttest.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "previews")
data class PhotoRoom(

    @ColumnInfo(name = "album_id")
    var albumId: Int? = null,

    @PrimaryKey
    var id: Int? = null,

    var title: String? = null,

    var url: String? = null,

    @ColumnInfo(name = "thumbnail_url")
    var thumbnailUrl: String? = null

)