package com.marzec.cheatday.extensions

import com.marzec.cheatday.view.model.ListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.joda.time.DateTime

fun emptyString() = ""

fun Long?.toDateTime() = this?.let { DateTime(it) }

fun Int.incIf(condition: () -> Boolean) = if (condition()) inc() else this

operator fun <T : ListItem> T.plus(list: List<T>): List<T> {
    return listOf(this) + list
}

operator fun <T : ListItem> T.plus(item: T): List<T> {
    return listOf(this) + listOf(item)
}

operator fun <T : ListItem> List<T>.plus(item: T): List<T> {
    return this + listOf(item)
}

fun <T1, T2, T3, R> combine(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    transform: suspend (T1, T2, T3) -> R
): Flow<R> =
    first.combine(second) { a, b -> a to b }
        .combine(third) { (a, b), c ->
            transform(a, b, c)
        }