package com.marzec.cheatday.extensions

import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.onIo() = subscribeOn(Schedulers.io())

fun <T> Observable<T>.onIo() = subscribeOn(Schedulers.io())

fun Completable.onIo() = subscribeOn(Schedulers.io())

fun <T> Observable<T>.toLiveData(
    backpressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST
) = LiveDataReactiveStreams.fromPublisher<T>(this.toFlowable(backpressureStrategy))