package com.marzec.cheatday

import com.marzec.cheatday.api.FeatureToggleApi
import com.marzec.cheatday.api.response.FeatureToggleDto

object LocalFeatureToggleApi : FeatureToggleApi {

    override suspend fun getById(id: Int): FeatureToggleDto {
        throw UnsupportedOperationException()
    }

    override suspend fun getAll(): List<FeatureToggleDto> = emptyList()
}