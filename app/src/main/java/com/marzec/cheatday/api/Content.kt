package com.marzec.cheatday.api

import android.util.Log
import com.marzec.cheatday.extensions.asInstance
import com.marzec.cheatday.extensions.asInstanceAndReturn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull

sealed class Content<T> {

    data class Data<T>(val data: T) : Content<T>()
    data class Loading<T>(val data: T? = null) : Content<T>()
    data class Error<T>(val exception: Exception) : Content<T>()
}

suspend fun <T> asContent(request: suspend () -> T): Content<T> {
    return try {
        Content.Data(request())
    } catch (e: Exception) {
        Log.e("Content", e.message.orEmpty(), e)
        Content.Error(e)
    }
}

fun <T> T.toContent(): Content<T> = Content.Data(this)

fun <T> T.toContentFlow(): Flow<Content<T>> = flowOf(Content.Data(this))

fun <T> asContentFlow(request: suspend () -> T): Flow<Content<T>> {
    return flow {
        emit(Content.Loading())
        try {
            emit(Content.Data(request()))
        } catch (e: Exception) {
            emit(Content.Error<T>(e))
        }
    }
}

suspend fun <T> Flow<Content<T>>.dataOrNull(): T? = filter { it !is Content.Loading }
    .singleOrNull()?.mapData()

fun <T> Flow<Content<T>>.unwrapContent(): Flow<T?> = filter { it !is Content.Loading }
    .map {
        if (it is Content.Data) {
            it.data
        } else {
            null
        }
    }

fun <T> Content.Error<T>.getMessage(): String = exception.message.orEmpty()

@Suppress("unchecked_cast")
fun <T> Content<T>.mapData(): T? =
    if (this is Content.Data<T>) {
        this.data
    } else {
        null
    }

@Suppress("unchecked_cast")
inline fun <reified T: Any> Content<T>.asData(action: Content.Data<T>.() -> Unit) =
    asInstance(action)

@Suppress("unchecked_cast")
inline fun <reified T: Any, R> Content<T>.asDataAndReturn(action: Content.Data<T>.() -> R) =
    asInstanceAndReturn(action)

@Suppress("unchecked_cast")
inline fun <reified T: Any> Content<T>.asError(action: Content.Error<T>.() -> Unit) =
    asInstance(action)

@Suppress("unchecked_cast")
inline fun <reified T: Any, R> Content<T>.asErrorAndReturn(action: Content.Error<T>.() -> R) =
    asInstanceAndReturn(action)
