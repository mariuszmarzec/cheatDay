package com.marzec.cheatday.stubs

import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.ui.DayState

fun stubDayState(
    day: Day,
    clicked: Boolean
) = DayState(
    day,
    clicked
)