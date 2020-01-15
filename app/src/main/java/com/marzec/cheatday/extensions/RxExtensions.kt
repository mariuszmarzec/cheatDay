package com.marzec.cheatday.extensions

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.onIo() = subscribeOn(Schedulers.io())