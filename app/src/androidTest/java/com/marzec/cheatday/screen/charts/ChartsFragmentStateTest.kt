package com.marzec.cheatday.screen.charts

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.screen.chart.ChartFragment
import com.marzec.cheatday.screen.chart.model.ChartsData
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
        ),
        averageWeights = emptyList()
    )
    val chartsState = State.Data(chartsData)
    val loadingState = State.Loading(chartsData)
    val errorState = State.Error(null, "Error has occurred")

    @Test
    fun initialState() = compareStateScreenshot<ChartFragment>(chartsState)

    @Test
    fun loadingState() = compareStateScreenshot<ChartFragment>(loadingState)

    @Test
    fun errorState() = compareStateScreenshot<ChartFragment>(errorState)
}
