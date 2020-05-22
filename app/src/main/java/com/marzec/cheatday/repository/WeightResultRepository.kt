package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.domain.toDb
import com.marzec.cheatday.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeightResultRepositoryImpl @Inject constructor(
    private val weightDao: WeightDao
) : WeightResultRepository {
    override fun observeWeights(userId: String): Flow<List<WeightResult>> =
        weightDao.observeWeights(userId).map { it.map(WeightResultEntity::toDomain) }

    override fun observeMinWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeMinWeight(userId).map { it?.toDomain() }

    override fun observeLastWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeLastWeight(userId).map { it?.toDomain() }

    override suspend fun putWeight(userId: String, weightResult: WeightResult) =
        weightDao.insertSuspend(weightResult.toDb(userId))

    override suspend fun updateWeight(userId: String, weightResult: WeightResult) =
        weightDao.updateSuspend(weightResult.toDb(userId))

    override suspend fun getWeight(id: Long): WeightResult? {
        return weightDao.getWeightSuspend(id).toDomain()
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