package com.marzec.cheatday.feature.home

import com.marzec.cheatday.di.FragmentScope
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterFragment
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterFragmentModule
import com.marzec.cheatday.feature.home.weights.WeightsFragment
import com.marzec.cheatday.feature.home.weights.WeightsFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [DaysCounterFragmentModule::class])
    @FragmentScope
    abstract fun bindDaysCounterFragment(): DaysCounterFragment

    @ContributesAndroidInjector(modules = [WeightsFragmentModule::class])
    @FragmentScope
    abstract fun bindWeightsFragment(): WeightsFragment
}