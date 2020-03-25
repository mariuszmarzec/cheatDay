package com.marzec.cheatday.domain

import com.marzec.cheatday.db.model.db.WeightResultEntity
import org.joda.time.DateTime

data class WeightResult(
    val id: Long,
    val value: Float,
    val date: DateTime
)

fun WeightResultEntity.toDomain() = WeightResult(id, value, DateTime(date))

fun WeightResult.toDb(userId: String) = WeightResultEntity(
    id,
    value,
    date.millis,
    userId
)