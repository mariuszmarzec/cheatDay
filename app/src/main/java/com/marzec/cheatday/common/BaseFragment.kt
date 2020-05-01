package com.marzec.cheatday.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        observe(this@BaseFragment, Observer {
            it?.let(observer)
        })
    }
}