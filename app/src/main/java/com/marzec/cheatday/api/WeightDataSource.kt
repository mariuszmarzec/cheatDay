package com.marzec.cheatday.api

import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.request.UpdateWeightDto
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.datasource.CommonDataSource

typealias WeightDataSource = CommonDataSource<Long, WeightDto, PutWeightRequest, UpdateWeightDto>

class WeightDataSourceImpl(private val api: WeightApi) : WeightDataSource {

    override suspend fun getAll(): List<WeightDto> = api.getAll()

    override suspend fun remove(id: Long) = api.remove(id)

    override suspend fun update(id: Long, update: UpdateWeightDto): WeightDto =
        api.update(id, update)

    override suspend fun create(create: PutWeightRequest): WeightDto =
        api.put(create)

    override suspend fun getById(id: Long): WeightDto = api.getAll().first { it.id.toLong() == id }
}