package com.marzec.cheatday.interactor

import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.extensions.incIf
import com.marzec.cheatday.repository.TargetWeightRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.repository.WeightResultRepository
import io.reactivex.BackpressureStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.reactive.asFlow
import org.joda.time.DateTime
import javax.inject.Inject

interface WeightInteractor {

    fun observeTargetWeight(): Flow<Float>

    fun observeMinWeight(): Flow<WeightResult?>

    fun setTargetWeight(weight: Float)

    fun observeWeights(): Flow<List<WeightResult>>

    suspend fun addWeight(weight: WeightResult)

    suspend fun updateWeight(weight: WeightResult)

    suspend fun getWeight(id: Long): WeightResult?
}

@ExperimentalCoroutinesApi
class WeightInteractorImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val targetRepository: TargetWeightRepository,
    private val weightResultRepository: WeightResultRepository,
    private val daysInteractor: DaysInteractor
) : WeightInteractor {

    override fun observeTargetWeight() = targetRepository
        .observeTargetWeight().toFlowable(BackpressureStrategy.BUFFER).asFlow()

    override fun observeMinWeight(): Flow<WeightResult?> {
        return userRepository.getCurrentUserFlow().flatMapLatest { user ->
            weightResultRepository.observeMinWeight(user.uuid)
        }
    }

    override fun setTargetWeight(weight: Float) = targetRepository.setTargetWeight(weight)

    override fun observeWeights(): Flow<List<WeightResult>> {
        return userRepository.getCurrentUserFlow().flatMapLatest { user ->
            weightResultRepository.observeWeights(user.uuid)
        }
    }

    override suspend fun addWeight(weight: WeightResult) {
        val userId = userRepository.getCurrentUserSuspend().uuid

        if (weight.date.withTimeAtStartOfDay() == DateTime.now().withTimeAtStartOfDay()) {
            weightResultRepository.observeLastWeight(userId).first()?.value?.let { old ->
                incrementCheatDaysIfNeeded(userId, weight, old)
            }
        }

        weightResultRepository.putWeight(userId, weight)
    }

    private suspend fun incrementCheatDaysIfNeeded(
        userId: String,
        weight: WeightResult,
        old: Float
    ) {
        val min = weightResultRepository.observeMinWeight(userId).first()!!.value
        val target =
            targetRepository.observeTargetWeight().toFlowable(BackpressureStrategy.BUFFER)
                .asFlow().first()
        val new = weight.value

        val newCheatDays = if (new > old && new > target) {
            -1 - (new.toInt() - old.toInt())
        } else if (new < old) {
            1 + (old.toInt() - new.toInt())
        } else {
            0
        }.incIf { min > new }

        if (newCheatDays != 0) {
            daysInteractor.incrementCheatDays(newCheatDays).blockingAwait()
        }
    }

    override suspend fun updateWeight(weight: WeightResult) {
        weightResultRepository.updateWeight(userRepository.getCurrentUserSuspend().uuid, weight)
    }

    override suspend fun getWeight(id: Long): WeightResult? {
        return weightResultRepository.getWeight(id)
    }
}