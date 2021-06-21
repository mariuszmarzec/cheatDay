package com.marzec.cheatday.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

fun <T> LiveData<T>.values(): List<T> = mutableListOf<T>()
    .apply { observeForever { add(it) } }

fun <T, R> LiveData<T>.values(sideEffects: LiveData<R>): List<Any> =
    mutableListOf<Any>().apply {
        MediatorLiveData<Any>().apply {
            addSource(this@values, ::setValue)
            addSource(sideEffects, ::setValue)
        }.observeForever { add(it) }
    }
