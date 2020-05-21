package com.marzec.cheatday.stubs

import com.marzec.cheatday.domain.Day

fun stubDay(
    id: Long = 0,
    type: Day.Type = Day.Type.CHEAT,
    count: Long = 0,
    max: Long = Long.MAX_VALUE
) = Day(
    id = id,
    type = type,
    count = count,
    max = max
)