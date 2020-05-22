package com.marzec.cheatday.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.marzec.cheatday.R
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

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

    inline fun <reified F : Fragment> replaceFragment() {
        val tag = F::class.java.simpleName
        val fragment: Fragment =
            parentFragmentManager.findFragmentByTag(tag) ?: F::class.java.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }
}