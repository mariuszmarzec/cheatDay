package com.marzec.cheatday.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    inline fun <T> Flow<T>.observe(crossinline action: (value: T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            collect {
                action(it)
            }
        }
    }
}
