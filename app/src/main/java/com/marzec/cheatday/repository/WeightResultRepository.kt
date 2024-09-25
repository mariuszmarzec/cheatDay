package com.marzec.cheatday.repository

import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.api.request.UpdateWeightDto
import com.marzec.cheatday.api.response.WeightDto
import com.marzec.cheatday.model.domain.UpdateWeight
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.content.Content
import com.marzec.content.mapData
import com.marzec.repository.CrudRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

typealias WeightCrudRepository = CrudRepository<Long, WeightResult, WeightResult, UpdateWeight, WeightDto, PutWeightRequest, UpdateWeightDto>

class WeightResultRepository @Inject constructor(
    private val repository: WeightCrudRepository
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
        repository.create(weightResult, policy = CrudRepository.RefreshPolicy.SEPARATE_DISPATCHER)

    suspend fun updateWeight(id: Long, weightResult: UpdateWeight): Flow<Content<Unit>> =
        repository.update(id, weightResult, policy = CrudRepository.RefreshPolicy.SEPARATE_DISPATCHER)

    suspend fun getWeight(id: Long): Flow<Content<WeightResult>> =
        repository.observeById(id)

    suspend fun removeWeight(id: Long): Flow<Content<Unit>> =
        repository.remove(id, policy = CrudRepository.RefreshPolicy.SEPARATE_DISPATCHER)

    private suspend fun observeWeightCacheFirst() = repository.observeAll()
}
