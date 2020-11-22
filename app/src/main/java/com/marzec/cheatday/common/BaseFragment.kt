package com.marzec.cheatday.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        observe(this@BaseFragment, Observer {
            it?.let(observer)
        })
    }

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(this@BaseFragment, Observer {
            observer(it)
        })
    }
}