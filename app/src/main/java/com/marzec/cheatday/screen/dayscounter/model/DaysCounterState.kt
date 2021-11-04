package com.marzec.cheatday.screen.dayscounter.model

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.ui.DayState

data class DaysCounterState(
    val cheat: DayState,
    val diet: DayState,
    val workout: DayState,
) {
    companion object {
        val DEFAULT_STATE = DaysCounterState(
            cheat = DayState(
                day = Day(
                    id = 0,
                    type = Day.Type.CHEAT,
                    count = 0L,
                    max = Constants.MAX_CHEAT_DAYS.toLong()
                ), clicked = false
            ), diet = DayState(
                Day(
                    id = 0,
                    type = Day.Type.DIET,
                    count = 0L,
                    max = Constants.MAX_DIET_DAYS.toLong()
                ), clicked = false
            ), workout = DayState(
                Day(
                    id = 0,
                    type = Day.Type.WORKOUT,
                    count = 0L,
                    max = Constants.MAX_WORKOUT_DAYS.toLong()
                ),
                clicked = false
            )
        )
    }
}
