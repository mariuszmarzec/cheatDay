package com.marzec.cheatday

import com.marzec.cheatday.di.DaggerTestAppComponent
import com.marzec.cheatday.di.TestAppComponent
import com.marzec.cheatday.di.TestViewModelModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class TestApp : App() {

    lateinit var component: TestAppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val builder = DaggerTestAppComponent.builder()
            .viewModelModule(TestViewModelModule)
            .app(this)
        component = builder.build()
        return component
    }
}