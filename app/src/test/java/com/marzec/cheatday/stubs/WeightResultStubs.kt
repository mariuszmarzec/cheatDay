package com.marzec.cheatday.stubs

import com.marzec.cheatday.api.Api.DATE_FORMATTER
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

fun stubWeightDto(
    id: Int = 0,
    value: Float = 0f,
    date: String = DateTime(0).toString(DATE_FORMATTER)
) = WeightDto(
    id = id,
    value = value,
    date = date
)

fun stubWeightResult(
    id: Long = 0,
    value: Float = 0f,
    date: DateTime = DateTime(0)
) = WeightResult(
    id = id,
    value = value,
    date = date
)

fun stubWeightResultEntity(
    id: Long = 0,
    value: Float = 0f,
    date: Long = 0,
    userId: Long = 0
) = WeightResultEntity(
    id,
    value,
    date,
    userId
)
