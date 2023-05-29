package com.ducpv.composeui.shared.extension

/**
 * Created by pvduc9773 on 28/05/2023.
 */
fun <T> List<T>.split(size: Int): List<List<T>> {
    val n = size.coerceAtLeast(1)
    val chunkSize = (this.size + n - 1) / n
    return chunked(chunkSize.coerceAtLeast(1))
}
