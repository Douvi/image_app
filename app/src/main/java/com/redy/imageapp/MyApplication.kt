package com.redy.imageapp

import android.app.Application
import com.redy.imageapp.network.NetworkService
import com.redy.imageapp.repository.ImageRepositoryImpl
import com.redy.imageapp.util.DispatchersIC

class MyApplication : Application() {

    companion object {
        private val service = NetworkService(baseURL = BuildConfig.URL).api
        val dispatchersIC = DispatchersIC(
            io = kotlinx.coroutines.Dispatchers.IO,
            ui = kotlinx.coroutines.Dispatchers.Main
        )

        val imageRepository = ImageRepositoryImpl(
            service = service,
            dispatcher = dispatchersIC
        )
    }
}
