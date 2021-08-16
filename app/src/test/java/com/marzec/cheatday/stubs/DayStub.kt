package com.marzec.cheatday.stubs

import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.model.domain.Day

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

fun stubDayEntity(
    id: Long = 0,
    type: String = "CHEAT",
    count: Long = 0,
    max: Long = Long.MAX_VALUE,
    userId: Long = ""
) = DayEntity(
    id = id,
    type = type,
    count = count,
    max = max,
    userId = userId
)