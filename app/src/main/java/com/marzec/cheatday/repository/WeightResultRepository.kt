package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.asContentFlow
import com.marzec.cheatday.api.mapData
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.toDomain
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDb
import com.marzec.cheatday.model.domain.toDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class WeightResultRepository @Inject constructor(
    private val weightApi: WeightApi,
    private val weightDao: WeightDao,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun observeWeights(): Flow<Content<List<WeightResult>>> =
        observeWeightCacheFirst()

    suspend fun observeMinWeight(): Flow<Content<WeightResult?>> =
        observeWeightCacheFirst().map { content ->
            content.mapData { weights ->
                weights.minByOrNull { it.value }
            }
        }

    suspend fun observeLastWeight(): Flow<Content<WeightResult?>> =
        observeWeightCacheFirst().map { content ->
            content.mapData { weights ->
                weights.maxByOrNull { it.date }
            }
        }

    suspend fun putWeight(weightResult: WeightResult): Flow<Content<WeightResult>> =
        asContentWithWeightsUpdate {
            weightApi.put(
                PutWeightRequest(
                    value = weightResult.value,
                    date = weightResult.date.toString(Api.DATE_FORMAT)
                )
            ).toDomain()
        }.flowOn(dispatcher)

    suspend fun updateWeight(weightResult: WeightResult): Flow<Content<Unit>> =
        asContentWithWeightsUpdate {
            weightApi.update(weightResult.id, weightResult.toDto())
        }.flowOn(dispatcher)

    suspend fun getWeight(id: Long): Flow<Content<WeightResult>> = asContentFlow {
        getWeightsFromApi().first { it.id == id }
    }.flowOn(dispatcher)

    private suspend fun getWeightsFromApi() = weightApi.getAll().map { it.toDomain() }

    suspend fun removeWeight(id: Long): Flow<Content<Unit>> =
        asContentWithWeightsUpdate { weightApi.remove(id) }.flowOn(dispatcher)

    private suspend fun cachedWeight(
        networkCall: suspend () -> Content<List<WeightResult>>,
    ): Flow<Content<List<WeightResult>>> {
        val userId = userRepository.getCurrentUser().id
        return cacheCall(
            networkCall = networkCall,
            cacheReadFlow = {
                weightDao.observeWeights(userId)
                    .map { weights -> weights.map { it.toDomain() }.takeIf { it.isNotEmpty() } }
            },
            saveToCache = { weights ->
                saveToCache(weights, userId)
            }
        )
    }

    private suspend fun saveToCache(
        weights: List<WeightResult>,
        userId: Long
    ) {
        weightDao.replaceAll(weights.map { it.toDb(userId, zeroId = true) })
    }

    private suspend fun observeWeightCacheFirst() =
        cachedWeight {
            asContent { getWeightsFromApi() }
        }.flowOn(dispatcher)

    @Suppress("unchecked_cast")
    private suspend fun <MODEL : Any> cacheCall(
        networkCall: suspend () -> Content<MODEL>,
        cacheReadFlow: suspend () -> Flow<MODEL?>,
        saveToCache: suspend (MODEL) -> Unit
    ): Flow<Content<MODEL>> =
        withContext(dispatcher) {
            val cached = cacheReadFlow().firstOrNull()
            val initial = if (cached != null) {
                Content.Data(cached)
            } else {
                Content.Loading()
            }
            merge(
                flow {
                    emit(initial)
                    val callResult = networkCall()
                    if (callResult is Content.Error) {
                        emit(callResult)
                    } else if (callResult is Content.Data) {
                        saveToCache(callResult.data)
                    }
                },
                cacheReadFlow().filterNotNull().map { Content.Data(it) }
            ).flowOn(dispatcher)
        }

    private suspend fun refreshWeightsCache() = asContent {
        getWeightsFromApi()
    }.let {
        if (it is Content.Data) {
            saveToCache(it.data, userRepository.getCurrentUser().id)
        }
    }


    private fun <T> asContentWithWeightsUpdate(request: suspend () -> T) =
        asContentFlow(request)
            .onEach {
                if (it is Content.Data) {
                    refreshWeightsCache()
                }
            }.flowOn(dispatcher)
}
