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

    fun observeMaxPossibleWeight(): Flow<Float>

    fun observeMinWeight(): Flow<WeightResult?>

    suspend fun setTargetWeight(weight: Float)

    suspend fun setMaxPossibleWeight(weight: Float)

    fun observeWeights(): Flow<Content<List<WeightResult>>>

    suspend fun addWeight(weight: WeightResult): Content<Unit>

    suspend fun updateWeight(weight: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?

    suspend fun removeWeight(id: Long)
}

@ExperimentalCoroutinesApi
class WeightInteractorImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val weightResultRepository: WeightResultRepository,
    private val daysInteractor: DaysInteractor
) : WeightInteractor {

    override fun observeTargetWeight() = userPreferencesRepository.observeTargetWeight()

    override fun observeMaxPossibleWeight(): Flow<Float> =
        userPreferencesRepository.observeMaxPossibleWeight()

    override fun observeMinWeight(): Flow<WeightResult?> {
        return userRepository.getCurrentUserFlow().flatMapLatest { user ->
            weightResultRepository.observeMinWeight(user.uuid)
        }
    }

    override suspend fun setTargetWeight(weight: Float) = userPreferencesRepository.setTargetWeight(weight)

    override suspend fun setMaxPossibleWeight(weight: Float) {
        userPreferencesRepository.setMaxPossibleWeight(weight)
    }

    override fun observeWeights(): Flow<Content<List<WeightResult>>> {
        return userRepository.getCurrentUserFlow().map { user ->
            weightResultRepository.observeWeights(user.uuid)
        }
    }

    override suspend fun addWeight(weight: WeightResult): Content<Unit> {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val lastValue = weightResultRepository.observeLastWeight(userId).first()?.value

        val result = weightResultRepository.putWeight(userId, weight)

        if (result is Content.Data) {
            if (weight.date.withTimeAtStartOfDay() == DateTime.now().withTimeAtStartOfDay()) {
                lastValue?.let { old -> incrementCheatDaysIfNeeded(userId, weight, old) }
            }
        }
        return result
    }

    private suspend fun incrementCheatDaysIfNeeded(
        userId: String,
        weight: WeightResult,
        old: Float
    ) {
        val min = weightResultRepository.observeMinWeight(userId).first()?.value ?: return
        val target =
            userPreferencesRepository.observeTargetWeight().first()
        val maxPossible =
            userPreferencesRepository.observeMaxPossibleWeight().first()
        val new = weight.value

        val newCheatDays = if (new > old && new > target) {
            -1 - (new.toInt() - old.toInt())
        } else if (new < old && new < maxPossible) {
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

    override suspend fun removeWeight(id: Long) {
        weightResultRepository.removeWeight(id)
    }
}