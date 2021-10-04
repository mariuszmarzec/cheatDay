package com.marzec.cheatday.screen.home.charts

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.api.toContentData
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.screen.chart.model.ChartsData
import com.marzec.cheatday.screen.dayscounter.DaysCounterFragment
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import com.marzec.cheatday.screen.login.view.LoginFragment
import com.marzec.mvi.State
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.joda.time.DateTime
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ChartsFragmentStateTest : ScreenshotTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val chartsData = ChartsData(
        weights = listOf(
            WeightResult(id = 9, value = 80f, date = DateTime.now()),
            WeightResult(id = 8, value = 82f, date = DateTime.now().minusDays(1)),
            WeightResult(id = 7, value = 83.5f, date = DateTime.now().minusDays(2))
        )
    )
    val chartsState = chartsData.toContentData()
    val loadingState = State.Loading(chartsData)
    val errorState = State.Error(null, "Error has occurred")

    @Test
    fun initialState() = compareStateScreenshot<DaysCounterFragment>(chartsState)

    @Test
    fun loadingState() = compareStateScreenshot<DaysCounterFragment>(loadingState)

    @Test
    fun errorState() = compareStateScreenshot<DaysCounterFragment>(errorState)
}
