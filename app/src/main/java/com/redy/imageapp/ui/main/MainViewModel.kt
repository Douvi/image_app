package com.redy.imageapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.redy.imageapp.network.ApiResponse
import com.redy.imageapp.util.DispatchersIC
import com.redy.imageapp.repository.ImageRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val imageRepository: ImageRepository,
    private val dispatcher: DispatchersIC
) : ViewModel() {

    companion object {
        fun create(
            imageRepository: ImageRepository,
            dispatcher: DispatchersIC
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                modelClass.getConstructor(
                    ImageRepository::class.java,
                    DispatchersIC::class.java
                ).newInstance(imageRepository, dispatcher)
        }
    }

    private val _items = MutableSharedFlow<ApiResponse<List<ItemRow>>>()
    val items = _items.asSharedFlow()

    fun loadData() {
        viewModelScope.launch(dispatcher.io) {
            when(val response = imageRepository.getListOfImages()) {
                is ApiResponse.Success -> {
                    val list = response.result.map { it.toItemRow() }
                    _items.emit(ApiResponse.Success(list))
                }
                is ApiResponse.Fail -> _items.emit(ApiResponse.Fail(response.error))
                else -> _items.emit(ApiResponse.Loading)
            }
        }
    }
}
