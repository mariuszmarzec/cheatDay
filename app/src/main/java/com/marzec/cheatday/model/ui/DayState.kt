package com.marzec.cheatday.model.ui

import com.marzec.cheatday.model.domain.Day

data class DayState(
    val day: Day,
    val clicked: Boolean
)