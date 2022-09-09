package com.redy.imageapp.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService(
    baseURL: String
) {
    private val retrofit: Retrofit = Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: NetworkApi = retrofit.create(NetworkApi::class.java)
}

sealed class ApiResponse<out T> {
    data class Success<T>(val result: T) : ApiResponse<T>()
    data class Fail(val error: Throwable) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

@SuppressWarnings("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ApiResponse<T> = withContext(dispatcher) {
    try {
        ApiResponse.Success(apiCall.invoke())
    } catch (t: Throwable) {
        ApiResponse.Fail(t)
    }
}
