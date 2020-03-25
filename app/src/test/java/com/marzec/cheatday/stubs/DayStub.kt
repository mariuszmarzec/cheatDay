package com.marzec.cheatday.stubs

import com.marzec.cheatday.domain.Day

object DayStub {

    fun create(
        id: Long = 0,
        type: Day.Type = Day.Type.CHEAT,
        count: Long = 0
    ) = Day(
        id = id,
        type = type,
        count = count
    )
}