package com.marzec.cheatday.feature.home.dayscounter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.marzec.cheatday.R
import com.marzec.cheatday.common.CustomFragmentInjection
import com.marzec.cheatday.di.TestViewModelFactoryModule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaysCounterFragmentTest {

    val context by lazy { InstrumentationRegistry.getInstrumentation().context }
    val viewModel = mock<DaysCounterViewModel>()

    @Before
    fun setUp() {
        val viewModelFactory = TestViewModelFactoryModule.viewModelFactory
        whenever(viewModelFactory.create<DaysCounterViewModel>(any())) doReturn viewModel
    }

    @After
    fun cleanUp() {
        CustomFragmentInjection.daggerInjectionEnabled = true
        CustomFragmentInjection.onTestInject = null
    }

    @Test
    fun cheat_days_are_present_on_screen() {
        CustomFragmentInjection.daggerInjectionEnabled = false

        val scenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme
        ) {
            DaysCounterFragment().apply {
                vmFactory = TestViewModelFactoryModule.createFactoryMock()
            }
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withText(R.string.home_counter_title_cheat_day))
        onView(withText(R.string.home_counter_title_diet_day))
        onView(withText(R.string.home_counter_title_workout_day))
    }
}