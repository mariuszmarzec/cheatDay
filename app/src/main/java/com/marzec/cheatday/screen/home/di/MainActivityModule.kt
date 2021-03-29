package com.marzec.cheatday.screen.home.di

import com.marzec.cheatday.di.FragmentScope
import com.marzec.cheatday.screen.addnewresult.AddNewWeightResultFragment
import com.marzec.cheatday.screen.addnewresult.di.AddNewWeightResultModule
import com.marzec.cheatday.screen.chart.ChartFragment
import com.marzec.cheatday.screen.chart.di.ChartFragmentModule
import com.marzec.cheatday.screen.dayscounter.DaysCounterFragment
import com.marzec.cheatday.screen.dayscounter.di.DaysCounterFragmentModule
import com.marzec.cheatday.screen.login.di.LoginFragmentModule
import com.marzec.cheatday.screen.login.view.LoginFragment
import com.marzec.cheatday.screen.weights.WeightsFragment
import com.marzec.cheatday.screen.weights.di.WeightsFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

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

    @ContributesAndroidInjector(modules = [ChartFragmentModule::class])
    @FragmentScope
    abstract fun bindChartFragment(): ChartFragment
}