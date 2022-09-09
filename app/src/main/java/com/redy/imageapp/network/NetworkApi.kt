package com.redy.imageapp.network

import com.redy.imageapp.network.ImageItemDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET("/list")
    suspend fun listOfImages(): List<ImageItemDTO>
}
