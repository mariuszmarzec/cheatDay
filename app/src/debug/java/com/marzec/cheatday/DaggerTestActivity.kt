package com.marzec.cheatday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marzec.cheatday.di.FragmentScope
import com.marzec.cheatday.screen.addnewresult.AddNewWeightResultFragment
import com.marzec.cheatday.screen.addnewresult.di.AddNewWeightResultModule
import com.marzec.cheatday.screen.dayscounter.DaysCounterFragment
import com.marzec.cheatday.screen.dayscounter.di.DaysCounterFragmentModule
import com.marzec.cheatday.screen.login.view.LoginFragment
import com.marzec.cheatday.screen.login.di.LoginFragmentModule
import com.marzec.cheatday.screen.weights.WeightsFragment
import com.marzec.cheatday.screen.weights.di.WeightsFragmentModule
import dagger.Module
import dagger.android.*
import javax.inject.Inject

class DaggerTestActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
    }

    override fun androidInjector(): AndroidInjector<Any?>? {
        return androidInjector
    }
}

@Module
abstract class DaggerTestActivityModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    @FragmentScope
    abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [DaysCounterFragmentModule::class])
    @FragmentScope
    abstract fun bindDaysCounterFragment(): DaysCounterFragment

    @ContributesAndroidInjector(modules = [WeightsFragmentModule::class])
    @FragmentScope
    abstract fun bindWeightsFragment(): WeightsFragment

    @ContributesAndroidInjector(modules = [AddNewWeightResultModule::class])
    @FragmentScope
    abstract fun bindAddNewWeightResultFragment(): AddNewWeightResultFragment
}