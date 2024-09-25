package com.marzec.cheatday.screen.weights

import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.core.getPaparazziRule
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.cheatday.screen.weights.model.WeightsMapper
import com.marzec.mvi.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeightsFragmentStateTest {

    @get:Rule
    val paparazzi = getPaparazziRule()

    val weightsData by lazy {
        WeightsData(
            minWeight = WeightResult(id = 9, value = 80f, date = DateTime.now()),
            weekAverage = 83f,
            maxPossibleWeight = 85f,
            targetWeight = 78f,
            weights = listOf(
                WeightResult(id = 9, value = 80f, date = DateTime.now()),
                WeightResult(id = 8, value = 82f, date = DateTime.now().minusDays(1)),
                WeightResult(id = 7, value = 83.5f, date = DateTime.now().minusDays(2))
            )
        )
    }
    val initialState = State.Data(weightsData)
    val loadingState = State.Loading<WeightsData>()
    val pendingActionState = State.Loading(weightsData)
    val errorState = State.Error(weightsData, "Error has occurred")

    val dispatcher = UnconfinedTestDispatcher()
    val mapper by lazy { WeightsMapper(paparazzi.context, dispatcher) }
    val renderer by lazy { WeightsRenderer(mapper, dispatcher) }

    @Before
    fun setUp() {
        DateTimeUtils.setCurrentMillisFixed(0)
    }

    @Test
    fun initialState() = compare(initialState)

    @Test
    fun loadingState() = compare(loadingState)

    @Test
    fun pendingActionState() = compare(pendingActionState)

    @Test
    fun errorState() = compare(errorState)

    private fun compare(state: State<WeightsData>) {
        val view = paparazzi.inflate<View>(R.layout.fragment_weights)
        with(renderer) {
            init(view)
            val uiItems = flowOf(state).mapToUi()
            render(runBlocking { uiItems.first() })
        }
        paparazzi.snapshot(view)
    }
}
