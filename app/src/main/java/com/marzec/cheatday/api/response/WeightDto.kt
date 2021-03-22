package com.marzec.cheatday.api.response

import com.marzec.cheatday.extensions.toDateTime
import com.marzec.cheatday.model.domain.WeightResult

data class WeightDto(
        val id: Int,
        val value: Float,
        val date: String
)

fun WeightDto.toDomain() = WeightResult(
        id = id.toLong(),
        value = value,
        date = date.toDateTime()
)
