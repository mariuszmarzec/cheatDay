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

fun <T1, T2> combine(
    first: Flow<T1>,
    second: Flow<T2>
): Flow<Pair<T1, T2>> =
    first.combine(second) { a, b -> Pair(a, b) }

fun <T1, T2, T3> combine(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
): Flow<Triple<T1, T2, T3>> =
    combine(first, second)
        .combine(third) { (a, b), c ->
            Triple(a, b, c)
        }