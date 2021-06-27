package com.marzec.cheatday.api

import android.util.Log
import com.marzec.cheatday.extensions.asInstance
import com.marzec.cheatday.extensions.asInstanceAndReturn

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

fun <T> Content.Error<T>.getMessage(): String = exception.message.orEmpty()

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
