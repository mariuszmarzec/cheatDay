package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.db.model.domain.WeightResult
import com.marzec.cheatday.db.model.domain.toDb
import com.marzec.cheatday.db.model.domain.toDomain
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class WeightResultRepositoryImpl @Inject constructor(
    private val weightDao: WeightDao
) : WeightResultRepository {
    override fun getWeights(userId: String): Observable<List<WeightResult>> =
        weightDao.getWeights(userId).map { it.map(WeightResultEntity::toDomain) }

    override fun putWeight(userId: String, weightResult: WeightResult): Completable {
        return weightDao.insertCompletable(weightResult.toDb(userId))
    }

    override fun editWeight(userId: String, weightResult: WeightResult): Completable {
        return weightDao.updateCompletable(weightResult.toDb(userId))
    }
}

interface WeightResultRepository {

    fun getWeights(userId: String): Observable<List<WeightResult>>

    fun putWeight(userId: String, weightResult: WeightResult): Completable

    fun editWeight(userId: String, weightResult: WeightResult): Completable
}