package com.marzec.cheatday.domain

import com.marzec.cheatday.db.model.db.DayEntity

data class Day(
    val id: Long,
    val type: Type,
    val count: Long
) {
    enum class Type {
        CHEAT,
        WORKOUT,
        DIET
    }
}

data class DaysGroup(
    val cheat: Day,
    val workOut: Day,
    val diet: Day
)

fun Day.toDb(userId: String): DayEntity {
    return DayEntity(
        this.id,
        this.type.name,
        this.count,
        userId
    )
}