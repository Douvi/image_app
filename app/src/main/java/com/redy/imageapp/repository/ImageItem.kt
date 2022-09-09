package com.redy.imageapp.repository

import com.redy.imageapp.BuildConfig
import com.redy.imageapp.network.ImageItemDTO
import java.io.Serializable

data class Size(
    val width: Int,
    val height: Int
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }

    val isHorizontal = width>height
}

data class ImageItem(
    val id: Int,
    val author: String,
    val size: Size
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }

    val url = BuildConfig.URL + "/${size.width}/${size.height}?image=$id"
}

fun ImageItemDTO.toImageItem(): ImageItem = ImageItem(
        id = this.id,
        author = this.author,
        size = Size(
            width = this.width,
            height = this.height
        )
    )
