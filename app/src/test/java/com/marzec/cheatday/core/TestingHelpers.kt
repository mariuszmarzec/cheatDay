package com.marzec.cheatday.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.values(scope: CoroutineScope): MutableList<T> {
    val values = mutableListOf<T>()
    val job = scope.launch {
        collect { values.add(it) }
    }
    job.cancel()
    return values
}
