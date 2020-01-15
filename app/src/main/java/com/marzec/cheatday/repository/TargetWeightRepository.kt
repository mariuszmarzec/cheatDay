package com.marzec.cheatday.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import javax.inject.Inject

class TargetWeightRepositoryImpl @Inject constructor(
    private val sharedPreferences: RxSharedPreferences
) : TargetWeightRepository {

    override fun setTargetWeight(weight: Float) {
        sharedPreferences.getFloat("weight").set(weight)
    }

    override fun observeTargetWeight(): Observable<Float> {
        return sharedPreferences.getFloat("weight").asObservable()
    }
}

interface TargetWeightRepository {

    fun setTargetWeight(weight: Float)

    fun observeTargetWeight(): Observable<Float>
}