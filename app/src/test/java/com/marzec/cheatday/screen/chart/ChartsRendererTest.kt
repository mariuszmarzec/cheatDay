package com.marzec.cheatday.screen.chart

import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.core.getPaparazziRule
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.screen.chart.model.ChartsData
import com.marzec.cheatday.screen.chart.renderer.ChartRenderer
import com.marzec.mvi.State
import org.joda.time.DateTime
import org.junit.Rule
import org.junit.Test

class ChartsRendererTest {

    @get:Rule
    val paparazzi = getPaparazziRule()

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
    val errorState = State.Error<ChartsData>(null, "Error has occurred")

    val renderer = ChartRenderer()

    @Test
    fun initialState() = compare(chartsState)

    @Test
    fun loadingState() = compare(loadingState)

    @Test
    fun errorState() = compare(errorState)

    private fun compare(state: State<ChartsData>) {
        val view = paparazzi.inflate<View>(R.layout.fragment_chart)
        renderer.init(view)
        renderer.render(state)
        paparazzi.snapshot(view)
    }
}
