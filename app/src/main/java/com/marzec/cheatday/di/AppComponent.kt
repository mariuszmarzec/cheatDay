package com.marzec.cheatday.di

import com.marzec.cheatday.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    RepositoryModule::class,
    InteractorModule::class,
    AppModule::class,
    ActivityBuilder::class,
    ApiModule::class,
    ViewModelModule::class,
    ViewModelFactoryModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}

