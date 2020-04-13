package com.marzec.cheatday.common

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun Completable.subscribeTillClear() {
        compositeDisposable.add(this.subscribe())
    }
}