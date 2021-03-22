package com.marzec.cheatday.api.response

import com.marzec.cheatday.api.Api.DATE_FORMATTER
import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

data class WeightDto(
        val id: Int,
        val value: Float,
        val date: String
)

fun WeightDto.toDomain() = WeightResult(
        id = id.toLong(),
        value = value,
        date = DateTime.parse(date, DATE_FORMATTER)
)