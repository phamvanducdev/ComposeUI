package com.ducpv.composeui.shared.extension

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Created by pvduc9773 on 28/05/2023.
 */
suspend fun <T, V> Iterable<T>.asyncAll(
    context: CoroutineContext = EmptyCoroutineContext,
    coroutine: suspend CoroutineScope.(T) -> V
): List<V> = coroutineScope {
    map { async(context) { coroutine(it) } }.awaitAll()
}
