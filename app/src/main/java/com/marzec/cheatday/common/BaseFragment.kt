package com.marzec.cheatday.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.marzec.cheatday.R
import com.marzec.cheatday.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        CustomFragmentInjection.inject(this)
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

abstract class BaseVMFragment<VM: ViewModel> : BaseFragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    @Inject
    lateinit var vmFactory: ViewModelFactory

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = vmFactory.create(this).create(viewModelClass())
    }

    abstract fun viewModelClass(): Class<out VM>
}

object CustomFragmentInjection {
    var daggerInjectionEnabled = true
    var onTestInject: (Fragment.() -> Unit)? = null
    fun inject(fragment: Fragment) {
        if (daggerInjectionEnabled) {
            AndroidSupportInjection.inject(fragment)
        } else {
            onTestInject?.invoke(fragment)
        }
    }
}