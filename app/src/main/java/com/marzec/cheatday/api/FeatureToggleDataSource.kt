package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.NewFeatureToggleDto
import com.marzec.cheatday.api.request.UpdateFeatureToggleDto
import com.marzec.cheatday.api.response.FeatureToggleDto
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.datasource.CommonDataSource
import javax.inject.Inject

class FeatureToggleDataSource @Inject constructor(private val api: FeatureToggleApi) :
    CommonDataSource<Int, FeatureToggleDto, NewFeatureToggleDto, UpdateFeatureToggleDto> {

    override suspend fun getAll(): List<FeatureToggleDto> = api.getAll()

    override suspend fun remove(id: Int) {
        throw UnsupportedOperationException()
    }

    override suspend fun update(id: Int, update: UpdateFeatureToggleDto): FeatureToggleDto {
        throw UnsupportedOperationException()
    }

    override suspend fun create(create: NewFeatureToggleDto): FeatureToggleDto {
        throw UnsupportedOperationException()
    }

    override suspend fun getById(id: Int): FeatureToggleDto = api.getById(id)
}