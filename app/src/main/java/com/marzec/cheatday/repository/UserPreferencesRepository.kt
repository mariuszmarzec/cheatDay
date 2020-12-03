package com.marzec.cheatday.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.marzec.cheatday.model.domain.Day
import io.reactivex.BackpressureStrategy

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.joda.time.DateTime
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
@FlowPreview
class UserPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: RxSharedPreferences,
    private val userRepository: UserRepository
) : UserPreferencesRepository {

    override suspend fun setTargetWeight(weight: Float) = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        sharedPreferences.getFloat("${userId}_weight").set(weight)
    }

    override fun observeTargetWeight(): Flow<Float> =
        userRepository.getCurrentUserFlow().flatMapMerge { user ->
            sharedPreferences.getFloat("${user.uuid}_weight").asObservable()
                .toFlowable(BackpressureStrategy.LATEST).asFlow().flowOn(Dispatchers.IO)
        }

    override fun observeWasClickToday(day: Day.Type): Flow<Boolean> =
        userRepository.getCurrentUserFlow().flatMapMerge { user ->
            sharedPreferences.getLong("${user.uuid}_$day", 0).asObservable().map { savedTime ->
                val today = DateTime.now().withTimeAtStartOfDay()
                today == DateTime(savedTime)
            }.toFlowable(BackpressureStrategy.BUFFER).asFlow().flowOn(Dispatchers.IO)
        }

    override suspend fun setWasClickedToday(day: Day.Type) = withContext(Dispatchers.IO) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val timeInMillis = DateTime.now().withTimeAtStartOfDay().millis
        sharedPreferences.getLong("${userId}_$day").set(timeInMillis)
    }
}

interface UserPreferencesRepository {

    suspend fun setTargetWeight(weight: Float)

    fun observeTargetWeight(): Flow<Float>

    fun observeWasClickToday(day: Day.Type): Flow<Boolean>

    suspend fun setWasClickedToday(day: Day.Type)
}