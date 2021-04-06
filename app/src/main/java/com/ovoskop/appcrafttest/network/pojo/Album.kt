package com.ovoskop.appcrafttest.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Album {

    @SerializedName("userId")
    @Expose
    var userId: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null

}