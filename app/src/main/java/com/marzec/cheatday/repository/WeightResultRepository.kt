package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.asContentFlow
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WeightResultRepository @Inject constructor(
    private val weightApi: WeightApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun observeWeights(): Content<List<WeightResult>> =
        withContext(dispatcher) {
            asContent { weightApi.getAll().map { it.toDomain() }.sortedByDescending { it.date } }
        }

    fun observeMinWeight(): Flow<Content<WeightResult?>> =
        asContentFlow {
            weightApi.getAll().map { it.toDomain() }.minByOrNull { it.value }
        }.flowOn(dispatcher)

    fun observeLastWeight(): Flow<Content<WeightResult?>> =
        asContentFlow {
            weightApi.getAll().map { it.toDomain() }.maxByOrNull { it.date }
        }.flowOn(dispatcher)

    suspend fun putWeight(weightResult: WeightResult): Flow<Content<Unit>> =
        asContentFlow {
            weightApi.put(
                PutWeightRequest(
                    value = weightResult.value,
                    date = weightResult.date.toString(Api.DATE_FORMAT)
                )
            )
        }.flowOn(dispatcher)

    suspend fun updateWeight(weightResult: WeightResult): Flow<Content<Unit>> =
        asContentFlow {
            weightApi.update(weightResult.id, weightResult.toDto())
        }.flowOn(dispatcher)

    suspend fun getWeight(id: Long): Flow<Content<WeightResult>> = asContentFlow {
        weightApi.getAll().map { it.toDomain() }.first { it.id == id }
    }.flowOn(dispatcher)

    suspend fun removeWeight(id: Long): Flow<Content<Unit>> =
        asContentFlow { weightApi.remove(id) }.flowOn(dispatcher)
}
