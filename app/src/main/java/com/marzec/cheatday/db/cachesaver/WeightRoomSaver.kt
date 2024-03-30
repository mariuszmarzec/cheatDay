package com.marzec.cheatday.db.cachesaver

import com.marzec.cache.ManyItemsCacheSaver
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.model.domain.toDomain
import com.marzec.cheatday.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class WeightRoomSaver(
    private val weightDao: WeightDao,
    private val userRepository: UserRepository
) : ManyItemsCacheSaver<Long, WeightResult> {

    override suspend fun getById(id: Long): WeightResult? = weightDao.getWeight(id)?.toDomain()

    override suspend fun observeCachedById(id: Long): Flow<WeightResult?> =
        weightDao.observeWeight(id).map { it?.toDomain() }

    override suspend fun updateItem(id: Long, data: WeightResult) {
        val userId = userRepository.getCurrentUser().id
        weightDao.updateSuspend(data.toDb(userId))
    }

    override suspend fun addItem(data: WeightResult) {
        val userId = userRepository.getCurrentUser().id
        weightDao.insert(data.toDb(userId))
    }

    override suspend fun removeItem(id: Long) {
        weightDao.removeById(id)
    }

    override suspend fun get(): List<WeightResult>? {
        val userId = userRepository.getCurrentUser().id
        return weightDao.observeWeights(userId)
            .firstOrNull()
            ?.takeIf { it.isNotEmpty() }
            ?.map { it.toDomain() }
    }

    override suspend fun observeCached(): Flow<List<WeightResult>?> {
        val userId = userRepository.getCurrentUser().id
        return weightDao.observeWeights(userId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun updateCache(update: (List<WeightResult>?) -> List<WeightResult>?) {
        throw NoSuchMethodException()
    }

    override suspend fun updateCache(data: List<WeightResult>) {
        val userId = userRepository.getCurrentUser().id
        weightDao.replaceAll(userId, data.map { it.toDb(userId) })
    }
}
