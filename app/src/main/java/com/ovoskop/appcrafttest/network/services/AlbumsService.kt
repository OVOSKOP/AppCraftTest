package com.ovoskop.appcrafttest.network.services

import com.ovoskop.appcrafttest.network.pojo.Album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumsService {

    @GET("albums")
    fun getAlbums(@Query("id") id: Int? = null) : Call<List<Album>>

}