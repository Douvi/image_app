package com.redy.imageapp.network

import com.google.gson.annotations.SerializedName

data class ImageItemDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("filename") val filename: String,
    @SerializedName("author") val author: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)