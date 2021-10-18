package com.marzec.cheatday.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.HiltTestActivity
import com.marzec.cheatday.screen.login.view.LoginFragment

inline fun <reified FRAGMENT : Fragment> launchFragmentWithState(
    state: Any,
    fragmentArgs: Bundle? = null
): Pair<ActivityScenario<HiltTestActivity>, FRAGMENT> {
    StateObserver.testState = state
    var fragment: FRAGMENT? = null
    val scenario = launchFragmentInDaggerContainer<FRAGMENT>(
        fragmentArgs = fragmentArgs
    ) {
        fragment = this as FRAGMENT
    }
    scenario.moveToState(Lifecycle.State.RESUMED)
    StateObserver.testState = null
    return scenario to fragment!!
}

inline fun <reified FRAGMENT : Fragment> ScreenshotTest.compareStateScreenshot(
    state: Any,
    calcHeightWithIds: List<Int>? = null,
    testName: String? = null,
    fragmentArgs: Bundle? = null
) {
    val (_, fragment) = launchFragmentWithState<FRAGMENT>(
        state,
        fragmentArgs
    )
    val activity = fragment.requireActivity()
    val height = calcHeightWithIds?.fold(0) { sum, viewId ->
        sum + activity.findViewById<View>(viewId).measuredHeight
    }
    compareScreenshot(activity = activity, heightInPx = height, name = testName)
}