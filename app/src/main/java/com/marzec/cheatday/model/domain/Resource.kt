package com.marzec.cheatday.model.domain

sealed class Resource<T>(
    open val data: T? = null,
    open val loading: Boolean = false,
    open val error: Throwable? = null
) {
    fun <T> success(data: T) = Success(data)

    fun <T> loading(data: T?) = Loading(data)

    fun <T> error(data: T? = null, error: Throwable) = Error(data, error)
}

data class Success<T>(
    override val data: T
) : Resource<T>(data, false, null)

data class Loading<T>(
    override val data: T? = null
) : Resource<T>(data, true, null)

data class Error<T>(
    override val data: T? = null,
    override val error: Throwable
) : Resource<T>(data, false, null)
