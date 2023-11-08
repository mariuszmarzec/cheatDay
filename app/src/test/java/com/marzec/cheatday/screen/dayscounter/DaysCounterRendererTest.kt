package com.marzec.cheatday.screen.dayscounter

import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.core.getPaparazziRule
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import org.junit.Rule
import org.junit.Test

class DaysCounterRendererTest {

    @get:Rule
    val paparazzi = getPaparazziRule()

    val cheat = DayState(day = Day(id = 0, type = Day.Type.CHEAT, count = 3, -1), clicked = false)
    val diet = DayState(day = Day(id = 0, type = Day.Type.DIET, count = 3, 5), clicked = false)
    val workout =
        DayState(day = Day(id = 0, type = Day.Type.WORKOUT, count = 7, 5), clicked = false)

    val initialState = DaysCounterState(
        cheat = cheat,
        diet = diet,
        workout = workout
    )

    val renderer = DaysCounterRenderer()

    @Test
    fun initialState() = compare(initialState)

    @Test
    fun clickedDaysState() = compare(
        initialState.copy(
            cheat = cheat.copy(clicked = true),
            workout = workout.copy(clicked = true)
        )
    )

    private fun compare(state: DaysCounterState) {
        val view = paparazzi.inflate<View>(R.layout.fragment_days_counter)
        renderer.init(view)
        renderer.render(state)
        paparazzi.snapshot(view)
    }
}
