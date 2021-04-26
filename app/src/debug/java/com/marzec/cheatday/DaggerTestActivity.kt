package com.marzec.cheatday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DaggerTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

//@Module
//abstract class DaggerTestActivityModule {
//
//    @FragmentScope
//    abstract fun bindLoginFragment(): LoginFragment
//
//    @FragmentScope
//    abstract fun bindDaysCounterFragment(): DaysCounterFragment
//
//    @FragmentScope
//    abstract fun bindWeightsFragment(): WeightsFragment
//
//    @FragmentScope
//    abstract fun bindAddNewWeightResultFragment(): AddNewWeightResultFragment
//}