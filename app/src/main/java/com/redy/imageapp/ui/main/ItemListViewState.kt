package com.redy.imageapp.ui.main

import com.redy.imageapp.repository.ImageItem

data class ItemListViewState(
    val toolbarTitle: String = "",
    val items: List<ItemRow> = emptyList()
)

data class ItemRow(
    val image: ImageItem
) {
    val name: String = image.author
    val isHorizontal = image.size.isHorizontal
}

fun ImageItem.toItemRow() = ItemRow(this)
