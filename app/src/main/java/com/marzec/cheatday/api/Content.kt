package com.marzec.cheatday.api

import android.util.Log
import com.marzec.cheatday.extensions.asInstance
import com.marzec.cheatday.extensions.asInstanceAndReturnOther
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
    } catch (expected: Exception) {
        Log.e("Content", expected.message.orEmpty(), expected)
        Content.Error(expected)
    }
}

fun <T> T.toContentData(): Content<T> = Content.Data(this)

fun <T> T.toContentFlow(): Flow<Content<T>> = flowOf(Content.Data(this))

fun <T> asContentFlow(request: suspend () -> T): Flow<Content<T>> {
    return flow {
        emit(Content.Loading())
        try {
            emit(Content.Data(request()))
        } catch (expected: Exception) {
            emit(Content.Error<T>(expected))
        }
    }
}

suspend fun <T> Flow<Content<T>>.dataOrNull(): T? = filter { it !is Content.Loading }
    .singleOrNull()?.unwrapData()

fun <T> Flow<Content<T>>.unwrapContent(): Flow<T?> = filter { it !is Content.Loading }
    .map {
        if (it is Content.Data) {
            it.data
        } else {
            null
        }
    }

fun <T> Content.Error<T>.getMessage(): String = exception.message.orEmpty()

fun <T, R> Content<T>.mapData(mapper: (T) -> R) = when (this) {
    is Content.Data -> try {
        Content.Data(mapper(this.data))
    } catch (expected: Exception) {
        Content.Error(expected)
    }
    is Content.Loading -> Content.Loading(this.data?.let(mapper))
    is Content.Error -> Content.Error(this.exception)
}

@Suppress("unchecked_cast")
fun <T> Content<T>.unwrapData(): T? =
    if (this is Content.Data<T>) {
        this.data
    } else {
        null
    }

@Suppress("unchecked_cast")
inline fun <reified T : Any> Content<T>.asData(action: Content.Data<T>.() -> Unit) =
    asInstance(action)

@Suppress("unchecked_cast")
inline fun <reified T : Any, R> Content<T>.asDataAndReturn(action: Content.Data<T>.() -> R) =
    asInstanceAndReturnOther(action)

@Suppress("unchecked_cast")
inline fun <reified T : Any> Content<T>.asError(action: Content.Error<T>.() -> Unit) =
    asInstance(action)

@Suppress("unchecked_cast")
inline fun <reified T : Any, R> Content<T>.asErrorAndReturn(action: Content.Error<T>.() -> R) =
    asInstanceAndReturnOther(action)

fun <R> combineContents(vararg contents: Content<*>, mapData: (List<*>) -> R): Content<R> =
    with(contents.toList()) {
        val errorContent = firstOrNull { it is Content.Error<*> } as? Content.Error
        if (errorContent != null) {
            return Content.Error(errorContent.exception)
        }
        val loadingContent = firstOrNull { it is Content.Loading<*> } as? Content.Loading
        if (loadingContent != null) {
            return Content.Loading()
        }
        return Content.Data(mapData(filterIsInstance<Content.Data<*>>().map { it.data }))
    }

@Suppress("unchecked_cast", "MagicNumber")
fun <T1, T2, T3, T4, R> combineContentsFlows(
    flow: Flow<Content<T1>>,
    flow2: Flow<Content<T2>>,
    flow3: Flow<Content<T3>>,
    flow4: Flow<Content<T4>>,
    mapData: (T1, T2, T3, T4) -> R
) = combine(
    flow = flow,
    flow2 = flow2,
    flow3 = flow3,
    flow4 = flow4,
    transform = { content1, content2, content3, content4 ->
        combineContents(content1, content2, content3, content4) { dataList ->
            mapData(
                dataList[0] as T1,
                dataList[1] as T2,
                dataList[2] as T3,
                dataList[3] as T4
            )
        }
    }
)
