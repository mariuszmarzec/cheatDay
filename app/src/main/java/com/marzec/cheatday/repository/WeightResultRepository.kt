package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
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

    fun observeMinWeight(): Flow<WeightResult?> =
        flow {
            emit(weightApi.getAll().map { it.toDomain() }.minByOrNull { it.value })
        }

    fun observeLastWeight(): Flow<WeightResult?> =
        flow {
            emit(weightApi.getAll().map { it.toDomain() }.maxByOrNull { it.date })
        }.flowOn(dispatcher)

    suspend fun putWeight(weightResult: WeightResult): Content<Unit> =
        withContext(dispatcher) {
            asContent {
                weightApi.put(
                    PutWeightRequest(
                        value = weightResult.value,
                        date = weightResult.date.toString(Api.DATE_FORMAT)
                    )
                )
            }
        }

    suspend fun updateWeight(weightResult: WeightResult): Content<Unit> = withContext(dispatcher) {
        asContent {
            weightApi.update(weightResult.toDto())
        }
    }

    suspend fun getWeight(id: Long): WeightResult? = withContext(dispatcher) {
        weightApi.getAll().map { it.toDomain() }.find { it.id == id }
    }

    suspend fun removeWeight(id: Long): Content<Unit> = withContext(dispatcher) {
        asContent { weightApi.remove(id) }
    }
}
