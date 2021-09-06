package com.marzec.cheatday.common

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.HiltTestActivity
import com.marzec.cheatday.screen.login.view.LoginFragment

fun <FRAGMENT> launchFragmentWithState(state: Any): Pair<ActivityScenario<HiltTestActivity>, FRAGMENT> {
    StateObserver.testState = state
    var fragment: FRAGMENT? = null
    val scenario = launchFragmentInDaggerContainer<LoginFragment>() {
        fragment = this as FRAGMENT
    }
    scenario.moveToState(Lifecycle.State.RESUMED)
    StateObserver.testState = null
    return scenario to fragment!!
}

fun <FRAGMENT : Fragment> ScreenshotTest.compareStateScreenshot(
    state: Any,
    calcHeightWithIds: List<Int>? = null,
    testName: String? = null
) {
    val (_, fragment) = launchFragmentWithState<FRAGMENT>(state)
    val activity = fragment.requireActivity()
    val height = calcHeightWithIds?.fold(0) { sum, viewId ->
        sum + activity.findViewById<View>(viewId).measuredHeight
    }
    compareScreenshot(activity = activity, heightInPx = height, name = testName)
}