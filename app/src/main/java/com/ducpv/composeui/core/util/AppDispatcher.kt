package com.ducpv.composeui.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface AppDispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val compute: CoroutineDispatcher
}

class AppDispatcherImpl : AppDispatcher {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val compute: CoroutineDispatcher = Dispatchers.Default
}
