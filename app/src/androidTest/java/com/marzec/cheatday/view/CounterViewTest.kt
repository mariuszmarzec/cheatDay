package com.marzec.cheatday.view

import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.marzec.cheatday.R
import org.junit.Test


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