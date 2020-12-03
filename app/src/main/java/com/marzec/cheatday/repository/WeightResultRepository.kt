package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.model.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WeightResultRepositoryImpl @Inject constructor(
    private val weightDao: WeightDao
) : WeightResultRepository {
    override fun observeWeights(userId: String): Flow<List<WeightResult>> =
        weightDao.observeWeights(userId).map { it.map(WeightResultEntity::toDomain) }.flowOn(
            Dispatchers.IO)

    override fun observeMinWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeMinWeight(userId).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    override fun observeLastWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeLastWeight(userId).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    override suspend fun putWeight(userId: String, weightResult: WeightResult) = withContext(Dispatchers.IO) {
        weightDao.insertSuspend(weightResult.toDb(userId))
    }

    override suspend fun updateWeight(userId: String, weightResult: WeightResult) = withContext(Dispatchers.IO) {
        weightDao.updateSuspend(weightResult.toDb(userId))
    }

    override suspend fun getWeight(id: Long): WeightResult? = withContext(Dispatchers.IO) {
        weightDao.getWeight(id)?.toDomain()
    }
}

interface WeightResultRepository {

    fun observeWeights(userId: String): Flow<List<WeightResult>>

    fun observeMinWeight(userId: String): Flow<WeightResult?>

    fun observeLastWeight(userId: String): Flow<WeightResult?>

    suspend fun putWeight(userId: String, weightResult: WeightResult)

    suspend fun updateWeight(userId: String, weightResult: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?
}