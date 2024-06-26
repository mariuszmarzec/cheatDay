package com.marzec.cheatday.model.domain

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.cheatday.db.model.db.WeightResultEntity
import org.joda.time.DateTime

data class WeightResult(
    val id: Long,
    val value: Float,
    val date: DateTime
)

fun WeightResultEntity.toDomain() = WeightResult(id, value, DateTime(date))

fun WeightResult.toDb(userId: Long) = WeightResultEntity(
    id,
    value,
    date.millis,
    userId
)

fun WeightResult.toDto(): WeightDto = WeightDto(id.toInt(), value, date.toString(Api.DATE_FORMAT))

fun WeightResult.toCreateDto(): PutWeightRequest = PutWeightRequest(value, date.toString(Api.DATE_FORMAT))
