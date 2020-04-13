package com.marzec.cheatday.common

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        observe(this@BaseFragment, Observer {
            it?.let(observer)
        })
    }
}