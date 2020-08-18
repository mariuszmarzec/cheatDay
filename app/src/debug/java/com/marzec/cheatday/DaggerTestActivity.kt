package com.marzec.cheatday

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.marzec.cheatday.di.FragmentScope
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultFragment
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultModule
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterFragment
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterFragmentModule
import com.marzec.cheatday.feature.home.weights.WeightsFragment
import com.marzec.cheatday.feature.home.weights.WeightsFragmentModule
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