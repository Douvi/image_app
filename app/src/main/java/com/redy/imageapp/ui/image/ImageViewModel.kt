package com.redy.imageapp.ui.image

import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redy.imageapp.repository.ImageItem
import com.redy.imageapp.util.connectFromViewBottomToViewBottom

class ImageViewModel(
    private val image: ImageItem?
) : ViewModel() {

    companion object {
        fun create(
            image: ImageItem?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                modelClass.getConstructor(
                    ImageItem::class.java
                ).newInstance(image)
        }
    }

    val author: String
        get() = image?.author ?: "Not Found"

    val imageUrl: String
        get() = image?.url ?: ""

    fun updateConstraint(
        constraintSet: ConstraintSet,
        fromViewId: Int,
        toViewId: Int) {
        if (isImageHorizontal) constraintSet.connectFromViewBottomToViewBottom(fromViewId, toViewId)
    }

    private val isImageHorizontal: Boolean
        get() = image?.size?.isHorizontal ?: false
}
