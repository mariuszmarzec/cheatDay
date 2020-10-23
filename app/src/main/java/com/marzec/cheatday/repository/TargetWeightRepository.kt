package com.marzec.cheatday.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import javax.inject.Inject

class TargetWeightRepositoryImpl @Inject constructor(
    private val sharedPreferences: RxSharedPreferences,
    private val userRepository: UserRepository
) : TargetWeightRepository {

    override suspend fun setTargetWeight(weight: Float) {
        val userId = userRepository.getCurrentUserSuspend().uuid
        sharedPreferences.getFloat("${userId}_weight").set(weight)
    }

    override fun observeTargetWeight(): Observable<Float> =
        userRepository.getCurrentUser().flatMapObservable { user ->
            sharedPreferences.getFloat("${user.uuid}_weight").asObservable()
        }
}

interface TargetWeightRepository {

    suspend fun setTargetWeight(weight: Float)

    fun observeTargetWeight(): Observable<Float>
}