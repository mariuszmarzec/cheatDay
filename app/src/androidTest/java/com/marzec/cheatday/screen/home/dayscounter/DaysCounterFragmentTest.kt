package com.marzec.cheatday.screen.home.dayscounter

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.facebook.testing.screenshot.Screenshot
import com.marzec.cheatday.R
import com.marzec.cheatday.common.launchFragmentInDaggerContainer
import com.marzec.cheatday.screen.dayscounter.DaysCounterFragment
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterViewModel
import com.nhaarman.mockitokotlin2.mock
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DaysCounterFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val viewModel = mock<DaysCounterViewModel>()

    @Before
    fun setUp() {
    }

    @Test
    fun cheat_days_are_present_on_screen() {
        lateinit var activity: FragmentActivity

        val scenario = launchFragmentInDaggerContainer<DaysCounterFragment> (
            themeResId = R.style.AppTheme
        )
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