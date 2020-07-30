package com.marzec.cheatday.di

import com.marzec.cheatday.App
import dagger.BindsInstance
import dagger.Component
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
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {
        fun viewModelFactoryModule(moduleFactory: ViewModelFactoryModule): Builder
        @BindsInstance
        fun app(App: App): Builder
        fun build(): TestAppComponent
    }
}
