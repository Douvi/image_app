package com.redy.imageapp

import com.redy.imageapp.network.ApiResponse
import com.redy.imageapp.repository.ImageItem
import com.redy.imageapp.repository.ImageRepository

class MockRepository(
    private val result: ApiResponse<List<ImageItem>>
) : ImageRepository {


    override suspend fun getListOfImages(): ApiResponse<List<ImageItem>> = result
}