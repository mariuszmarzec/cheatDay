package com.marzec.adapterdelegate.payload

data class NullablePayload<T>(val value: T?)

fun <T> NullablePayload<T>?.applyIfNonNull(action: (T?) -> Unit) {
    if (this != null) {
        action(value)
    }
}