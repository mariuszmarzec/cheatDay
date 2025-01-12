package com.marzec.cheatday.api.response

import com.marzec.featuretoggle.FeatureToggle

data class FeatureToggleDto(
    val id: Int,
    val name: String,
    val value: String
)

fun FeatureToggleDto.toDomain() = FeatureToggle(id, name, value)
