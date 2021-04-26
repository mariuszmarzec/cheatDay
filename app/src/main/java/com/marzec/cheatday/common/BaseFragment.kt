package com.marzec.cheatday.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        observe(viewLifecycleOwner) {
            it?.let(observer)
        }
    }

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(viewLifecycleOwner) {
            observer(it)
        }
    }
}
