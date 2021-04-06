package com.ovoskop.appcrafttest.network.services

import com.ovoskop.appcrafttest.network.pojo.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PreviewsService {

    @GET("photos")
    fun getPreviews(@Query("albumId") albumId: Int) : Call<List<Photo>>

}