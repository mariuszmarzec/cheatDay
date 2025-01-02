package com.marzec.cheatday.api.request

import com.marzec.cheatday.model.domain.NewFeatureToggle

data class NewFeatureToggleDto(val name: String, val value:String)

fun NewFeatureToggleDto.toDomain() = NewFeatureToggle(name, value)
