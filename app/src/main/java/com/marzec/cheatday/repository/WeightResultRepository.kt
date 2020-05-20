package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.domain.toDb
import com.marzec.cheatday.domain.toDomain
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeightResultRepositoryImpl @Inject constructor(
    private val weightDao: WeightDao
) : WeightResultRepository {
    override fun getWeights(userId: String): Flow<List<WeightResult>> =
        weightDao.getWeights(userId).map { it.map(WeightResultEntity::toDomain) }

    override suspend fun putWeight(userId: String, weightResult: WeightResult) {
        weightDao.insertSuspend(weightResult.toDb(userId))
    }

    override suspend fun editWeight(userId: String, weightResult: WeightResult) {
        weightDao.updateSuspend(weightResult.toDb(userId))
    }
}

interface WeightResultRepository {

    fun getWeights(userId: String): Flow<List<WeightResult>>

    suspend fun putWeight(userId: String, weightResult: WeightResult)

    suspend fun editWeight(userId: String, weightResult: WeightResult)
}