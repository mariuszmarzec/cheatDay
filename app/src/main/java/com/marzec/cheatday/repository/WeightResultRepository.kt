package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.model.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WeightResultRepositoryImpl @Inject constructor(
    private val weightDao: WeightDao,
    private val userPreferencesRepository: DataStoreUserPreferencesRepository,
    private val weightApi: WeightApi
) : WeightResultRepository {
    override suspend fun observeWeights(userId: String): Content<List<WeightResult>> =
        withContext(Dispatchers.IO) {
            asContent { weightApi.getAll().map { it.toDomain() }.sortedByDescending { it.date } }
        }

    override fun observeMinWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeMinWeight(userId).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    override fun observeLastWeight(userId: String): Flow<WeightResult?> =
        weightDao.observeLastWeight(userId).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    override suspend fun putWeight(userId: String, weightResult: WeightResult, forceApi: Boolean) =
        withContext(Dispatchers.IO) {
            asContent {
                if (forceApi || userPreferencesRepository.isWeightsMigrated()) {
                    weightApi.put(
                        PutWeightRequest(
                            value = weightResult.value,
                            date = weightResult.date.toString(Api.DATE_FORMAT)
                        )
                    )
                } else {
                    weightDao.insertSuspend(weightResult.toDb(userId))
                }
            }
        }

    override suspend fun updateWeight(userId: String, weightResult: WeightResult) =
        withContext(Dispatchers.IO) {
            weightDao.updateSuspend(weightResult.toDb(userId))
        }

    override suspend fun getWeight(id: Long): WeightResult? = withContext(Dispatchers.IO) {
        weightDao.getWeight(id)?.toDomain()
    }
}

interface WeightResultRepository {

    suspend fun observeWeights(userId: String): Content<List<WeightResult>>

    fun observeMinWeight(userId: String): Flow<WeightResult?>

    fun observeLastWeight(userId: String): Flow<WeightResult?>

    suspend fun putWeight(userId: String, weightResult: WeightResult, forceApi: Boolean = false): Content<Unit>

    suspend fun updateWeight(userId: String, weightResult: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?
}