package com.marzec.cheatday.feature.home.dayscounter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.marzec.cheatday.R
import com.marzec.cheatday.di.TestViewModelModule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class DaysCounterFragmentTest {

    val context by lazy { InstrumentationRegistry.getInstrumentation().context }

    @Before
    fun setUp() {
        val viewModelFactory = TestViewModelModule.viewModelFactory
        whenever(viewModelFactory.create(DaysCounterViewModel::class.java)) doReturn mock()
    }

    @Test
    fun cheat_days_are_present_on_screen() {
        val scenario = launchFragmentInContainer<DaysCounterFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withText(context.getString(R.string.home_counter_title_cheat_day)))
        onView(withText(context.getString(R.string.home_counter_title_diet_day)))
        onView(withText(context.getString(R.string.home_counter_title_workout_day)))
    }
}