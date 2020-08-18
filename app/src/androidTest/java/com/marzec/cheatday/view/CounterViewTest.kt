package com.marzec.cheatday.view

import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.marzec.cheatday.R
import org.junit.Test

/*
To ensure working ui tests:
https://github.com/facebook/screenshot-tests-for-android/issues/224

adb shell settings put global hidden_api_policy_p_apps 1
adb shell settings put global hidden_api_policy_pre_p_apps 1
adb shell settings put global hidden_api_policy  1 // this is for Q+
 */
class CounterViewTest {

    @Test
    fun increaseMode() {
        val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
        val view = CounterView(context, null, R.style.AppTheme).apply {
            setMax(5)
            setTitle(R.string.home_counter_title_diet_day)
            setValue(3)
            setMode(CounterView.CountMode.INCREASE.toString())
        }

        ViewHelpers.setupView(view)
            .setExactWidthDp(300)
            .layout()

        Screenshot.snap(view)
            .setName("${CounterViewTest::class.java.simpleName}_increaseMode")
            .record();
    }
}