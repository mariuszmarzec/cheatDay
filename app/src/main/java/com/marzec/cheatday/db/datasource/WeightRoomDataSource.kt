package com.marzec.cheatday.db.datasource

import android.content.res.Resources.NotFoundException
import com.marzec.cheatday.api.WeightDataSource
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.request.UpdateWeightDto
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.extensions.toDateTime
import com.marzec.cheatday.model.domain.UpdateWeight
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.model.domain.toDomain
import com.marzec.featuretoggle.toDto
import com.marzec.cheatday.repository.UserRepository
import com.marzec.datasource.CommonDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class CommonWeightRoomDataSource @Inject constructor(
    private val userRepository: UserRepository,
    private val weightDao: WeightDao
) : CommonDataSource<Long, WeightResult, WeightResult, UpdateWeight> {

    override suspend fun getById(id: Long): WeightResult = weightDao.getWeight(id)?.toDomain()
        ?: throw NotFoundException("Weight not found")

    override suspend fun getAll(): List<WeightResult> {
        val userId = userRepository.getCurrentUser().id
        return weightDao.observeWeights(userId)
            .firstOrNull()
            .orEmpty()
            .map { it.toDomain() }
    }

    override suspend fun remove(id: Long) {
        weightDao.removeById(id)
    }

    override suspend fun update(id: Long, update: UpdateWeight): WeightResult {
        val userId = userRepository.getCurrentUser().id
        val entity = weightDao.getWeight(id) ?: throw NotFoundException("Weight not found")
        val updatedEntity = WeightResultEntity(
            id = id,
            value = update.value ?: entity.value,
            date = update.date?.millis ?: entity.date,
            userId = userId
        )
        weightDao.updateSuspend(updatedEntity)
        return updatedEntity.toDomain()
    }

    override suspend fun create(create: WeightResult): WeightResult {
        val userId = userRepository.getCurrentUser().id
        val newId = weightDao.insertSuspend(create.toDb(userId))
        return weightDao.getWeight(newId)?.toDomain() ?: throw NotFoundException("Weight not found")
    }
}

class WeightRoomDataSource @Inject constructor(
    private val dataSource: CommonWeightRoomDataSource
) : WeightDataSource {

    override suspend fun getAll(): List<WeightDto> = dataSource.getAll().map { it.toDto() }

    override suspend fun remove(id: Long) {
        dataSource.remove(id)
    }

    override suspend fun update(id: Long, update: UpdateWeightDto): WeightDto =
        dataSource.update(id, UpdateWeight(update.value, update.date?.toDateTime())).toDto()

    override suspend fun create(create: PutWeightRequest): WeightDto =
        dataSource.create(WeightResult(0, create.value, create.date.toDateTime())).toDto()

    override suspend fun getById(id: Long): WeightDto = dataSource.getById(id).toDto()
}
