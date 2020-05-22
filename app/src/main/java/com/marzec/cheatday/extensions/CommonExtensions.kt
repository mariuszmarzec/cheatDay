package com.marzec.cheatday.extensions

import com.marzec.cheatday.view.model.ListItem
import org.joda.time.DateTime

fun Long?.toDateTime() = this?.let { DateTime(it) }

fun Int.incIf(condition: () -> Boolean) = if (condition()) inc() else this

operator fun <T: ListItem> T.plus(list: List<T>): List<T> {
    return listOf(this) + list
}

operator fun <T: ListItem> T.plus(item: T): List<T> {
    return listOf(this) + listOf(item)
}

operator fun <T: ListItem> List<T>.plus(item: T): List<T> {
    return this + listOf(item)
}