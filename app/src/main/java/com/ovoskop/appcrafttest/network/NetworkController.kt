package com.ovoskop.appcrafttest.network

import com.ovoskop.appcrafttest.network.pojo.Album
import com.ovoskop.appcrafttest.network.pojo.Photo
import com.ovoskop.appcrafttest.network.services.AlbumsService
import com.ovoskop.appcrafttest.network.services.PreviewsService
import com.ovoskop.appcrafttest.utils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkController {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getPreviews(albumId : Int) : List<Photo> {

        return suspendCoroutine { continuation ->

            val previewsService: PreviewsService = retrofit.create(PreviewsService::class.java)

            val call = previewsService.getPreviews(albumId)

            call.enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {

                    if (response.isSuccessful) {
                        val listPreviews = response.body()

                        listPreviews?.apply {
                            continuation.resume(this)
                        }

                    } else {
                        println("error ${response.errorBody()?.string()}")

                        continuation.resumeWithException(Exception())
                    }

                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    println("error ${t.localizedMessage}")
                    continuation.resumeWithException(t)
                }


            })

        }

    }


    suspend fun getAlbum(id: Int) : Album {

        return suspendCoroutine { continuation ->

            val albumService: AlbumsService = retrofit.create(AlbumsService::class.java)

            val call = albumService.getAlbums(id)

            call.enqueue(object : Callback<List<Album>> {

                override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {

                    if (response.isSuccessful) {
                        val albums = response.body()

                        albums?.apply {
                            continuation.resume(this.first())
                        }

                    } else {
                        println("error ${response.errorBody()?.string()}")
                        continuation.resumeWithException(Exception())
                    }

                }

                override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                    println("error ${t.localizedMessage}")
                    continuation.resumeWithException(t)
                }

            })

        }

    }

    suspend fun getAlbums() : List<Album> {

        return suspendCoroutine { continuation ->

            val albumService: AlbumsService = retrofit.create(AlbumsService::class.java)

            val call = albumService.getAlbums()

            call.enqueue(object : Callback<List<Album>> {

                override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {

                    if (response.isSuccessful) {
                        val albums = response.body()

                        albums?.apply {
                            continuation.resume(this)
                        }

                    } else {
                        println("error ${response.errorBody()?.string()}")
                        continuation.resumeWithException(Exception())
                    }

                }

                override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                    println("error ${t.localizedMessage}")
                    continuation.resumeWithException(t)
                }

            })

        }

    }
}