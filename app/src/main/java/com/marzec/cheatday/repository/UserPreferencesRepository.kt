package com.marzec.cheatday.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.marzec.cheatday.model.domain.Day
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.joda.time.DateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: RxSharedPreferences,
    private val userRepository: UserRepository
) : UserPreferencesRepository {

    override suspend fun setTargetWeight(weight: Float) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        sharedPreferences.getFloat("${userId}_weight").set(weight)
    }

    override fun observeTargetWeight(): Observable<Float> =
        userRepository.getCurrentUser().flatMapObservable { user ->
            sharedPreferences.getFloat("${user.uuid}_weight").asObservable()
        }

    override fun observeWasClickToday(day: Day.Type): Flow<Boolean> =
        userRepository.getCurrentUser().flatMapObservable { user ->
            sharedPreferences.getLong("${user.uuid}_$day", 0).asObservable().map { savedTime ->
                val today = DateTime.now().withTimeAtStartOfDay()
                today == DateTime(savedTime)
            }
        }.toFlowable(BackpressureStrategy.BUFFER).asFlow()

    override suspend fun setWasClickedToday(day: Day.Type) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        val timeInMillis = DateTime.now().withTimeAtStartOfDay().millis
        sharedPreferences.getLong("${userId}_$day").set(timeInMillis)
    }


}

interface UserPreferencesRepository {

    suspend fun setTargetWeight(weight: Float)

    fun observeTargetWeight(): Observable<Float>

    fun observeWasClickToday(day: Day.Type): Flow<Boolean>

    suspend fun setWasClickedToday(day: Day.Type)
}