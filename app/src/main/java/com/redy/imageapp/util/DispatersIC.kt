package com.redy.imageapp.util

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val io: CoroutineDispatcher
    val ui: CoroutineDispatcher
}

class DispatchersIC(
    override val io: CoroutineDispatcher,
    override val ui: CoroutineDispatcher
) : Dispatchers
