package com.marzec.cheatday.feature.home.dayscounter

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.marzec.cheatday.R
import com.marzec.cheatday.common.launchFragmentInDaggerContainer
import com.marzec.cheatday.di.TestViewModelFactoryModule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaysCounterFragmentTest {

    val viewModel = mock<DaysCounterViewModel>()

    @Before
    fun setUp() {
        val viewModelFactory = TestViewModelFactoryModule.viewModelFactory
        whenever(viewModelFactory.create<DaysCounterViewModel>(any())) doReturn viewModel
    }

    @Test
    fun cheat_days_are_present_on_screen() {
        lateinit var activity: FragmentActivity

        val scenario = launchFragmentInDaggerContainer<DaysCounterFragment> (
            themeResId = R.style.AppTheme
        ) {
            DaysCounterFragment().apply {
                vmFactory = TestViewModelFactoryModule.createFactoryMock()
            }
        }
        scenario.onActivity { activity = it }
        scenario.moveToState(Lifecycle.State.RESUMED)

//        onView(withText(R.string.home_counter_title_cheat_day)).check(matches(isDisplayed()))
//        onView(withText(R.string.home_counter_title_diet_day)).check(matches(isDisplayed()))
//        onView(withText(R.string.home_counter_title_workout_day)).check(matches(isDisplayed()))

        Screenshot.snapActivity(activity)
            .setName("${DaysCounterFragment::class.java.simpleName}_cheat_days_are_present_on_screen")
            .record();
    }
}