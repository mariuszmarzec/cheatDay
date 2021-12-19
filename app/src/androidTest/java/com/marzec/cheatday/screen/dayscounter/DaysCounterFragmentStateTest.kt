package com.marzec.cheatday.screen.dayscounter

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.PolicySetupRule
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.screen.dayscounter.DaysCounterFragment
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import com.marzec.cheatday.screen.login.view.LoginFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DaysCounterFragmentStateTest : ScreenshotTest {

    @get:Rule
    var policySetupRule = PolicySetupRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val cheat = DayState(day = Day(id = 0, type = Day.Type.CHEAT, count = 3, -1), clicked = false)
    val diet = DayState(day = Day(id = 0, type = Day.Type.DIET, count = 3, 5), clicked = false)
    val workout =
        DayState(day = Day(id = 0, type = Day.Type.WORKOUT, count = 7, 5), clicked = false)

    val initialState = DaysCounterState(
        cheat = cheat,
        diet = diet,
        workout = workout
    )

    @Test
    fun initialState() = compareStateScreenshot<DaysCounterFragment>(initialState)

    @Test
    fun clickedDaysState() = compareStateScreenshot<DaysCounterFragment>(
        initialState.copy(
            cheat = cheat.copy(clicked = true),
            workout = workout.copy(clicked = true)
        )
    )
}
