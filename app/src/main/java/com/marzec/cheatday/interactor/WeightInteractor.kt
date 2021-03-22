package com.marzec.cheatday.interactor

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.extensions.incIf
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.repository.WeightResultRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import org.joda.time.DateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.map

interface WeightInteractor {

    fun observeTargetWeight(): Flow<Float>

    fun observeMinWeight(): Flow<WeightResult?>

    suspend fun setTargetWeight(weight: Float)

    fun observeWeights(): Flow<Content<List<WeightResult>>>

    suspend fun addWeight(weight: WeightResult): Content<Unit>

    suspend fun updateWeight(weight: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?
}

@ExperimentalCoroutinesApi
class WeightInteractorImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val targetRepository: UserPreferencesRepository,
    private val weightResultRepository: WeightResultRepository,
    private val daysInteractor: DaysInteractor
) : WeightInteractor {

    override fun observeTargetWeight() = targetRepository.observeTargetWeight()

    override fun observeMinWeight(): Flow<WeightResult?> {
        return userRepository.getCurrentUserFlow().flatMapLatest { user ->
            weightResultRepository.observeMinWeight(user.uuid)
        }
    }

    override suspend fun setTargetWeight(weight: Float) = targetRepository.setTargetWeight(weight)

    override fun observeWeights(): Flow<Content<List<WeightResult>>> {
        return userRepository.getCurrentUserFlow().map { user ->
            weightResultRepository.observeWeights(user.uuid)
        }
    }

    override suspend fun addWeight(weight: WeightResult): Content<Unit> {
        val userId = userRepository.getCurrentUserSuspend().uuid

        val result = weightResultRepository.putWeight(userId, weight)

        if (result is Content.Data) {
            if (weight.date.withTimeAtStartOfDay() == DateTime.now().withTimeAtStartOfDay()) {
                weightResultRepository.observeLastWeight(userId).first()?.value?.let { old ->
                    incrementCheatDaysIfNeeded(userId, weight, old)
                }
            }
        }
        return result
    }

    private suspend fun incrementCheatDaysIfNeeded(
        userId: String,
        weight: WeightResult,
        old: Float
    ) {
        val min = weightResultRepository.observeMinWeight(userId).first()!!.value
        val target =
            targetRepository.observeTargetWeight().first()
        val new = weight.value

        val newCheatDays = if (new > old && new > target) {
            -1 - (new.toInt() - old.toInt())
        } else if (new < old) {
            1 + (old.toInt() - new.toInt())
        } else {
            0
        }.incIf { min > new }

        if (newCheatDays != 0) {
            daysInteractor.incrementCheatDays(newCheatDays)
        }
    }

    override suspend fun updateWeight(weight: WeightResult) {
        weightResultRepository.updateWeight(userRepository.getCurrentUserSuspend().uuid, weight)
    }

    override suspend fun getWeight(id: Long): WeightResult? {
        return weightResultRepository.getWeight(id)
    }
}