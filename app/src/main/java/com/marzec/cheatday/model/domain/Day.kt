package com.marzec.cheatday.model.domain

import com.marzec.cheatday.db.model.db.DayEntity

data class Day(
    val id: Long,
    val type: Type,
    val count: Long,
    val max: Long
) {
    enum class Type {
        CHEAT,
        WORKOUT,
        DIET
    }
}

data class DaysGroup(
    val cheat: Day,
    val workout: Day,
    val diet: Day
)

fun Day.toDb(userId: String): DayEntity {
    return DayEntity(
        id = this.id,
        type = this.type.name,
        count = this.count,
        max = this.max,
        userId = userId
    )
}