package com.marzec.cheatday.interactor

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asContentFlow
import com.marzec.cheatday.api.dataOrNull
import com.marzec.cheatday.extensions.incIf
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.repository.WeightResultRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.joda.time.DateTime

class WeightInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val weightResultRepository: WeightResultRepository,
    private val daysInteractor: DaysInteractor
) {

    fun observeTargetWeight() = userPreferencesRepository.observeTargetWeight()

    fun observeMaxPossibleWeight(): Flow<Float> =
        userPreferencesRepository.observeMaxPossibleWeight()

    fun observeMinWeight(): Flow<Content<WeightResult?>> =
        userRepository.observeCurrentUser().flatMapLatest { user ->
            weightResultRepository.observeMinWeight()
        }

    suspend fun setTargetWeight(weight: Float) = userPreferencesRepository.setTargetWeight(weight)

    suspend fun setMaxPossibleWeight(weight: Float) {
        userPreferencesRepository.setMaxPossibleWeight(weight)
    }

    fun observeWeights(): Flow<Content<List<WeightResult>>> {
        return userRepository.observeCurrentUser().map { user ->
            weightResultRepository.observeWeights()
        }
    }

    suspend fun addWeight(weight: WeightResult): Flow<Content<Unit>> = asContentFlow {
        val lastValue = weightResultRepository.observeLastWeight().dataOrNull()?.value
        val minBeforeNewAdded = weightResultRepository.observeMinWeight().dataOrNull()?.value
        val result = weightResultRepository.putWeight(weight).dataOrNull()

        if (result != null) {
            if (weight.date.withTimeAtStartOfDay() == DateTime.now().withTimeAtStartOfDay()) {
                lastValue?.let { old ->
                    incrementCheatDaysIfNeeded(minBeforeNewAdded, weight, old)
                }
            }
        } else {
            throw Error()
        }
    }

    private suspend fun incrementCheatDaysIfNeeded(
        minBeforeNewAdded: Float?,
        weight: WeightResult,
        old: Float
    ) {
        if (minBeforeNewAdded == null) return
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
        }.incIf { minBeforeNewAdded > new }

        if (newCheatDays != 0) {
            daysInteractor.incrementCheatDays(newCheatDays)
        }
    }

    suspend fun updateWeight(weight: WeightResult): Flow<Content<Unit>> =
        weightResultRepository.updateWeight(weight)

    suspend fun getWeight(id: Long): Flow<Content<WeightResult>> =
        weightResultRepository.getWeight(id)

    suspend fun removeWeight(id: Long): Flow<Content<Unit>> =
        weightResultRepository.removeWeight(id)
}