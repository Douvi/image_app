package com.redy.imageapp

import androidx.constraintlayout.widget.ConstraintSet
import com.redy.imageapp.network.ApiResponse
import com.redy.imageapp.repository.Size
import com.redy.imageapp.repository.ImageItem
import com.redy.imageapp.ui.image.ImageViewModel
import com.redy.imageapp.ui.main.MainViewModel
import com.redy.imageapp.util.DispatchersIC
import com.redy.imageapp.util.connectFromViewBottomToViewBottom
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    @Test
    fun validateConstraintLayoutBottom() {
        val toLayout = 2
        val constraintSet = ConstraintSet()
        constraintSet.connectFromViewBottomToViewBottom(1, toLayout)

        // Make sure we have one time on the list
        assertEquals(1, constraintSet.knownIds.size)

        val constraint = constraintSet.getConstraint(constraintSet.knownIds[0])
        assertEquals(toLayout, constraint.layout.bottomToBottom)
    }

    @Test
    fun validateImageViewModelWhenVerticalImageIsNull() {
        val imageViewModel = ImageViewModel.create(null).create(ImageViewModel::class.java)

        val toLayout = 2
        val constraintSet = ConstraintSet()
        imageViewModel.updateConstraint(constraintSet, 1, toLayout)

        // Make sure we have one time on the list
        assertEquals(0, constraintSet.knownIds.size)
    }

    @Test
    fun validateImageViewModelWhenVerticalImage() {
        val imageViewModel = ImageViewModel.create(
            ImageItem(0, "test", Size(50, 100))
        ).create(ImageViewModel::class.java)

        val toLayout = 2
        val constraintSet = ConstraintSet()
        imageViewModel.updateConstraint(constraintSet, 1, toLayout)

        // Make sure we have zero time on the list
        assertEquals(0, constraintSet.knownIds.size)
    }

    @Test
    fun validateImageViewModelWhenHorizontalImage() {
        val imageViewModel = ImageViewModel.create(
            ImageItem(0, "test", Size(100, 50))
        ).create(ImageViewModel::class.java)

        val toLayout = 2
        val constraintSet = ConstraintSet()
        imageViewModel.updateConstraint(constraintSet, 1, toLayout)

        // Make sure we have one time on the list
        assertEquals(1, constraintSet.knownIds.size)

        val constraint = constraintSet.getConstraint(constraintSet.knownIds[0])
        assertEquals(toLayout, constraint.layout.bottomToBottom)
    }

}
