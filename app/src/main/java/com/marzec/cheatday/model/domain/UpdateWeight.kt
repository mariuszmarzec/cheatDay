package com.marzec.cheatday.model.domain

import com.marzec.cheatday.api.Api.DATE_FORMAT
import com.marzec.cheatday.api.request.UpdateWeightDto
import org.joda.time.DateTime

data class UpdateWeight(
    val value: Float?,
    val date: DateTime?
)

fun UpdateWeight.toDto() = UpdateWeightDto(
    value,
    date?.toString(DATE_FORMAT)
)