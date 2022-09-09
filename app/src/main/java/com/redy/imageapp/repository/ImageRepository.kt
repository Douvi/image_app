package com.redy.imageapp.repository

import com.redy.imageapp.network.ApiResponse
import com.redy.imageapp.network.NetworkApi
import com.redy.imageapp.network.safeApiCall
import com.redy.imageapp.util.DispatchersIC

interface ImageRepository {
    suspend fun getListOfImages(): ApiResponse<List<ImageItem>>
}

class ImageRepositoryImpl(
    private val service: NetworkApi,
    private val dispatcher: DispatchersIC
) : ImageRepository {
    override suspend fun getListOfImages(): ApiResponse<List<ImageItem>> =
        safeApiCall(dispatcher = dispatcher.io) {
            service.listOfImages().map { it.toImageItem() }
        }
}
