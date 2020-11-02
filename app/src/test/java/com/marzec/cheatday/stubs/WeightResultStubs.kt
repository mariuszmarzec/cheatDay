package com.marzec.cheatday.stubs

import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

fun stubWeightResult(
    id: Long = 0,
    value: Float = 0f,
    date: DateTime = DateTime(0)
) = WeightResult(
    id = id,
    value = value,
    date = date
)
