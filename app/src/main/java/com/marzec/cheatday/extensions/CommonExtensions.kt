package com.marzec.cheatday.extensions

import com.marzec.cheatday.api.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import org.joda.time.DateTime

const val EMPTY_STRING = ""

fun Long?.toDateTime() = this?.let { DateTime(it) }

fun Int.incIf(condition: () -> Boolean) = if (condition()) inc() else this

fun <T1, T2> combine(
    first: Flow<T1>,
    second: Flow<T2>
): Flow<Pair<T1, T2>> =
    combine(first, second) { a, b -> Pair(a, b) }

fun <T1, T2, T3> combine(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
): Flow<Triple<T1, T2, T3>> =
    combine(first, second, third) { a, b, c ->
        Triple(a, b, c)
    }

fun <T1, T2, T3, T4> combine(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    fourth: Flow<T4>,
): Flow<Quadruple<T1, T2, T3, T4>> =
    combine(first, second, third, fourth) { a, b, c, d ->
        Quadruple(a, b, c, d)
    }

fun String.toDateTime(): DateTime = DateTime.parse(this, Api.DATE_FORMATTER)

@Suppress("unchecked_cast")
inline fun <reified T : Any> Any.asInstance(action: T.() -> Unit) = (this as? T)?.action()

@Suppress("unchecked_cast")
inline fun <reified T : Any, R> Any.asInstanceAndReturnOther(action: T.() -> R) =
    (this as? T)?.action()

data class Quadruple<T1, T2, T3, T4>(
    val ob1: T1,
    val ob2: T2,
    val ob3: T3,
    val ob4: T4
)

fun <T> T.asFlow() = flowOf(this)

fun Boolean?.orFalse() = this ?: false
