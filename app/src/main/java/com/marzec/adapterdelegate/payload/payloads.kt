package com.marzec.adapterdelegate.payload

fun <T> payload(newValue: T, oldValue: T): T? =
    if (oldValue != newValue) newValue else null

fun <T> nullablePayload(newValue: T?, oldValue: T?): NullablePayload<T>? =
    if (oldValue != newValue) NullablePayload(newValue) else null
