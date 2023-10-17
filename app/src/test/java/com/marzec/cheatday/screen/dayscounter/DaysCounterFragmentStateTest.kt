package com.marzec.cheatday.screen.dayscounter

import android.Manifest
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
//class DaysCounterFragmentStateTest : ScreenshotTest {
//
//    @get:Rule
//    var runtimePermissionRule: GrantPermissionRule =
//        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//    @get:Rule
//    var policySetupRule = PolicySetupRule()
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    val cheat = DayState(day = Day(id = 0, type = Day.Type.CHEAT, count = 3, -1), clicked = false)
//    val diet = DayState(day = Day(id = 0, type = Day.Type.DIET, count = 3, 5), clicked = false)
//    val workout =
//        DayState(day = Day(id = 0, type = Day.Type.WORKOUT, count = 7, 5), clicked = false)
//
//    val initialState = DaysCounterState(
//        cheat = cheat,
//        diet = diet,
//        workout = workout
//    )
//
//    @Test
//    fun initialState() = compareStateScreenshot<DaysCounterFragment>(initialState)
//
//    @Test
//    fun clickedDaysState() = compareStateScreenshot<DaysCounterFragment>(
//        initialState.copy(
//            cheat = cheat.copy(clicked = true),
//            workout = workout.copy(clicked = true)
//        )
//    )
//}
