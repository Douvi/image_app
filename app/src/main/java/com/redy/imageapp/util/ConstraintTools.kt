package com.redy.imageapp.util

import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintSet.connectFromViewBottomToViewBottom(from: Int, to: Int) {
    this.connect(from, ConstraintSet.BOTTOM, to, ConstraintSet.BOTTOM)
}
