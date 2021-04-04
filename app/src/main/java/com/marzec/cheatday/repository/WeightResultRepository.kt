package com.marzec.cheatday.repository

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.asContent
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.cheatday.api.response.toDomain
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WeightResultRepositoryImpl @Inject constructor(
    private val weightApi: WeightApi
) : WeightResultRepository {
    override suspend fun observeWeights(userId: String): Content<List<WeightResult>> =
        withContext(Dispatchers.IO) {
            asContent { weightApi.getAll().map { it.toDomain() }.sortedByDescending { it.date } }
        }

    override fun observeMinWeight(userId: String): Flow<WeightResult?> =
        flow {
            emit(weightApi.getAll().map { it.toDomain() }.minByOrNull { it.value })
        }

    override fun observeLastWeight(userId: String): Flow<WeightResult?> =
         flow {
             emit(weightApi.getAll().map { it.toDomain() }.maxByOrNull { it.date })
         }.flowOn(Dispatchers.IO)

    override suspend fun putWeight(userId: String, weightResult: WeightResult) =
        withContext(Dispatchers.IO) {
            asContent {
                weightApi.put(
                    PutWeightRequest(
                        value = weightResult.value,
                        date = weightResult.date.toString(Api.DATE_FORMAT)
                    )
                )
            }
        }

    override suspend fun updateWeight(userId: String, weightResult: WeightResult) =
        withContext(Dispatchers.IO) {
            weightApi.update(weightResult.toDto())
        }

    override suspend fun getWeight(id: Long): WeightResult? = withContext(Dispatchers.IO) {
        weightApi.getAll().map { it.toDomain() }.find { it.id == id }
    }
}

interface WeightResultRepository {

    suspend fun observeWeights(userId: String): Content<List<WeightResult>>

    fun observeMinWeight(userId: String): Flow<WeightResult?>

    fun observeLastWeight(userId: String): Flow<WeightResult?>

    suspend fun putWeight(userId: String, weightResult: WeightResult): Content<Unit>

    suspend fun updateWeight(userId: String, weightResult: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?
}