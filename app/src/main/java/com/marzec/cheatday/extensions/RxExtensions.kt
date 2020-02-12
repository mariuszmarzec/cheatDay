package com.marzec.cheatday.extensions

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.onIo() = subscribeOn(Schedulers.io())

fun <T> Observable<T>.onIo() = subscribeOn(Schedulers.io())

fun Completable.onIo() = subscribeOn(Schedulers.io())