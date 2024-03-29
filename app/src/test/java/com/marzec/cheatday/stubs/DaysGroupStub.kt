package com.marzec.cheatday.stubs

import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup

fun stubDaysGroup(
    cheat: Day = stubDay(),
    workOut: Day = stubDay(),
    diet: Day = stubDay()
) = DaysGroup(
    cheat = cheat,
    workout = workOut,
    diet = diet
)
