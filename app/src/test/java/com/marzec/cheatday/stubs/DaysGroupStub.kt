package com.marzec.cheatday.stubs

import com.marzec.cheatday.domain.Day
import com.marzec.cheatday.domain.DaysGroup

object DaysGroupStub {

    fun create(
        cheat: Day = DayStub.create(),
        workOut: Day = DayStub.create(),
        diet: Day = DayStub.create()
    ) = DaysGroup(
        cheat = cheat,
        workOut = workOut,
        diet = diet
    )
}